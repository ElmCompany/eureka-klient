//package eureka
//
//import helpers.json.toJson
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class ModelTest {
//
//    val instance = Instance(
//        instanceId = "123",
//        hostName = "localhost",
//        app = "eureka-client-test",
//        ipAddr = "192.168.1.10",
//        secureVipAddress = "192.168.1.11",
//        status = StatusType.UP,
//        vipAddress = "192.168.1.12",
//        securePort = 0,
//        homePageUrl = "http://localhost/home-page-url",
//        statusPageUrl = "http://localhost/status-page-url",
//        healthCheckUrl = "http://localhost/health-check-url",
//        dataCenterInfo = DataCenterInfo(DcNameType.MY_OWN, "")
//    )
//
//    @Test
//    fun `test serialization`() {
//
//        val expected = """{
//              "instanceId": "123",
//              "app": "eureka-client-test",
//              "dataCenterInfo": {
//                "@class":"",
//                "metadata": null,
//                "name": "MyOwn"
//              },
//              "healthCheckUrl": "http://localhost/health-check-url",
//              "homePageUrl": "http://localhost/home-page-url",
//              "hostName": "localhost",
//              "ipAddr": "192.168.1.10",
//              "leaseInfo": null,
//              "metadata": null,
//              "port": null,
//              "securePort": 0,
//              "secureVipAddress": "192.168.1.11",
//              "status": "UP",
//              "statusPageUrl": "http://localhost/status-page-url",
//              "vipAddress": "192.168.1.12"
//            }""".replace("\\s+".toRegex(), "")
//
//        assertEquals(expected, instance.toJson())
//    }
//
//    @Test
//    fun `test serialization ignoreNull`() {
//
//        val expected = """{
//             "instanceId": "123",
//              "app": "eureka-client-test",
//              "dataCenterInfo": {
//                "@class":"",
//                "name": "MyOwn"
//              },
//              "healthCheckUrl": "http://localhost/health-check-url",
//              "homePageUrl": "http://localhost/home-page-url",
//              "hostName": "localhost",
//              "ipAddr": "192.168.1.10",
//              "securePort": 0,
//              "secureVipAddress": "192.168.1.11",
//              "status": "UP",
//              "statusPageUrl": "http://localhost/status-page-url",
//              "vipAddress": "192.168.1.12"
//            }""".replace("\\s+".toRegex(), "")
//
//        assertEquals(expected, instance.toJson(ignoreNull = true))
//    }
//}