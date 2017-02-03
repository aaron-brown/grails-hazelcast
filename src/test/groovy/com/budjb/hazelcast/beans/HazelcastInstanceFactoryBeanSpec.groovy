package com.budjb.hazelcast.beans

import com.budjb.hazelcast.HazelcastInstanceService
import com.hazelcast.config.Config
import com.hazelcast.core.HazelcastInstance
import spock.lang.Specification

class HazelcastInstanceFactoryBeanSpec extends Specification {
    void 'When a HazelcastInstanceFactoryBean is created but is missing the instanceName, an IllegalArgumentException is thrown'() {
        setup:
        HazelcastInstanceFactoryBean factoryBean = new HazelcastInstanceFactoryBean()

        when:
        factoryBean.afterPropertiesSet()

        then:
        thrown IllegalArgumentException
    }

    void 'When a HazelcastInstanceFactoryBean is created, it returns the correct Hazelcast instance'() {
        setup:
        Config config = new Config()
        config.instanceName = 'tmp'
        config.networkConfig.join.multicastConfig.enabled = false

        HazelcastInstanceService hazelcastInstanceService = new HazelcastInstanceService([])
        HazelcastInstance instance = hazelcastInstanceService.createInstance(config)

        HazelcastInstanceFactoryBean factoryBean = new HazelcastInstanceFactoryBean()
        factoryBean.hazelcastInstanceService = hazelcastInstanceService
        factoryBean.instanceName = 'tmp'
        factoryBean.afterPropertiesSet()

        when:
        Object object = factoryBean.getObject()

        then:
        object instanceof HazelcastInstance
        ((HazelcastInstance) object).config.instanceName == 'tmp'
        object == instance

        cleanup:
        instance.shutdown()
    }
}
