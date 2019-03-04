package fun.hercules.order.order.platform.order.utils

import fun.hercules.order.order.platform.order.model.PayChannel
import spock.lang.Specification

class ChargeStatusMappingTest extends Specification {

    def "should find Chinese characters product charge status"() {

        expect: "status is correct"
        status.equals(ChargeStatusMapping.getProductChargeStatus(productChargeSettled))

        where:
        productChargeSettled | status
        false                | "待收取"
        true                 | "已收取"

    }

    def "should find Chinese characters service charge status"() {

        expect: "status is correct"
        status.equals(ChargeStatusMapping.getServiceChargeStatus(productChargeSettled, payChannel))

        where:
        productChargeSettled | payChannel           | status
        false                | PayChannel.LOGISTICS | "待结算"
        true                 | PayChannel.LOGISTICS | "已结算"
        true                 | PayChannel.WMS       | "无需结算"

    }

    def "should find Chinese characters commission charge status"() {

        expect: "status is correct"
        status.equals(ChargeStatusMapping.getCommissionChargeStatus(productChargeSettled, payChannel))

        where:
        productChargeSettled | payChannel           | status
        false                | PayChannel.LOGISTICS | "待扣除"
        true                 | PayChannel.LOGISTICS | "已扣除"
        false                | PayChannel.WMS       | "待收取"
        true                 | PayChannel.WMS       | "已收取"
    }

    def "should find product settled"() {

        expect: "status is correct"
        productChargeSettled == ChargeStatusMapping.fromProductChargeStatus(status)

        where:
        productChargeSettled | status
        false                | "待收取"
        true                 | "已收取"
    }

    def "should find service settled"() {

        expect: "status is correct"
        productChargeSettled == ChargeStatusMapping.fromServiceChargeStatus(status, payChannel)

        where:
        productChargeSettled | payChannel           | status
        false                | PayChannel.LOGISTICS | "待结算"
        true                 | PayChannel.LOGISTICS | "已结算"
        true                 | PayChannel.WMS       | "无需结算"

    }

    def "should find commission settled"() {

        expect: "status is correct"
        productChargeSettled == ChargeStatusMapping.fromCommissionChargeStatus(status, payChannel)

        where:
        productChargeSettled | payChannel           | status
        false                | PayChannel.LOGISTICS | "待扣除"
        true                 | PayChannel.LOGISTICS | "已扣除"
        false                | PayChannel.WMS       | "待收取"
        true                 | PayChannel.WMS       | "已收取"
    }
}
