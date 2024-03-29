package helpers.json

import kotlin.reflect.KCallable
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.extensionReceiverParameter
import kotlin.reflect.full.memberFunctions

private val stringType by lazy {
    arrayOf(
        String::class.createType(), String::class.createType(nullable = true)
    )
}

private val numberTypes by lazy {
    arrayOf(
        Byte::class.createType(), Byte::class.createType(nullable = true),
        Short::class.createType(), Short::class.createType(nullable = true),
        Int::class.createType(), Int::class.createType(nullable = true),
        Long::class.createType(), Long::class.createType(nullable = true),
        Float::class.createType(), Float::class.createType(nullable = true),
        Byte::class.createType(), Byte::class.createType(nullable = true)
    )
}

fun serialize(obj: Any?, ignoreNull: Boolean): String? = obj?.let {

    val jsonStr = callToJsonFunction(obj)
    if (jsonStr != null)
        return@let jsonStr

    val properties = obj::class.members
        .filter { isProperty(obj, it) }

    if (properties.isNotEmpty()) {
        return@let properties
            .map { serialize(obj, it, ignoreNull) }
            .filter { !ignoreNull || it != null }
            .filterNotNull()
            .joinToString(prefix = "{", separator = ",", postfix = "}")
    }

    if (obj is Enum<*>) { // enum with no properties
        return@let obj.name.doubleQuote()
    }

    return@let null
}

fun serialize(obj: Any?, property: KCallable<*>, ignoreNull: Boolean): String? {
    val value = when {
        property.returnType in numberTypes -> property.call(obj)
        property.returnType in stringType -> (property.call(obj) as String?)?.doubleQuote()
        property.returnType.arguments.isNotEmpty()
                && isCollection(property.returnType) -> serializeCollection(property, obj, ignoreNull)
        else -> serialize(property.call(obj), ignoreNull)
    }

    return if (ignoreNull && value == null) null
    else property.name.doubleQuote() + ":" + value
}

fun serializeCollection(property: KCallable<*>, obj: Any?, ignoreNull: Boolean) =
    (property.call(obj) as Collection<*>)
        .map { serialize(it, ignoreNull) }
        .joinToString(prefix = "[", separator = ",", postfix = "]")

private fun isProperty(obj: Any?, it: KCallable<*>): Boolean {
    val enumInartisticProps = obj is Enum<*> && it.name in arrayOf("name", "ordinal")
    val isProperty = it.extensionReceiverParameter == null && it is KProperty1<*, *>
    return isProperty && !enumInartisticProps
}

private fun isCollection(returnType: KType) =
    returnType in arrayOf(
        Collection::class.createType(returnType.arguments),
        Set::class.createType(returnType.arguments),
        List::class.createType(returnType.arguments)
    )


private fun callToJsonFunction(obj: Any?): String? = obj?.let {
    obj::class.memberFunctions
        .find {
            it.name == "toJson" &&
                    it.returnType in stringType &&
                    it.parameters.size == 1
        }
        ?.call(obj) as String?
}

/**
 * Serializes object to json.
 * It can be customized by providing a member function [toJson(): String]
 * for any object in the object graph
 */
fun Any.toJson(ignoreNull: Boolean = false): String? = serialize(this, ignoreNull)

fun String.doubleQuote() = """"$this""""