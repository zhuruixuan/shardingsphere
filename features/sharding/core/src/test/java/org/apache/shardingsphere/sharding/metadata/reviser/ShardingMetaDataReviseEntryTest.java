/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sharding.metadata.reviser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.apache.shardingsphere.infra.config.props.ConfigurationProperties;
import org.apache.shardingsphere.infra.instance.InstanceContext;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.metadata.reviser.column.ShardingColumnGeneratedReviser;
import org.apache.shardingsphere.sharding.metadata.reviser.constraint.ShardingConstraintReviser;
import org.apache.shardingsphere.sharding.metadata.reviser.index.ShardingIndexReviser;
import org.apache.shardingsphere.sharding.metadata.reviser.schema.ShardingSchemaTableAggregationReviser;
import org.apache.shardingsphere.sharding.metadata.reviser.table.ShardingTableNameReviser;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.apache.shardingsphere.test.fixture.jdbc.MockedDataSource;
import org.junit.jupiter.api.Test;

public class ShardingMetaDataReviseEntryTest {
    
    private final ShardingMetaDataReviseEntry reviseEntry = new ShardingMetaDataReviseEntry();
    
    private final ShardingRule rule = createShardingRule();
    
    @Test
    void assertGetIndexReviser() {
        Optional<ShardingIndexReviser> indexReviser = reviseEntry.getIndexReviser(rule, "t_order0");
        assertTrue(indexReviser.isPresent());
        assertThat(indexReviser.get().getClass(), is(ShardingIndexReviser.class));
    }
    
    @Test
    void assertGetColumnGeneratedReviser() {
        Optional<ShardingColumnGeneratedReviser> columnGeneratedReviser = reviseEntry.getColumnGeneratedReviser(rule, "t_order0");
        assertTrue(columnGeneratedReviser.isPresent());
        assertThat(columnGeneratedReviser.get().getClass(), is(ShardingColumnGeneratedReviser.class));
    }
    
    @Test
    void assertGetConstraintReviser() {
        Optional<ShardingConstraintReviser> constraintReviser = reviseEntry.getConstraintReviser(rule, "t_order1");
        assertTrue(constraintReviser.isPresent());
        assertThat(constraintReviser.get().getClass(), is(ShardingConstraintReviser.class));
    }
    
    @Test
    void assertGetTableNameReviser() {
        Optional<ShardingTableNameReviser> tableNameReviser = reviseEntry.getTableNameReviser();
        assertTrue(tableNameReviser.isPresent());
        assertThat(tableNameReviser.get().getClass(), is(ShardingTableNameReviser.class));
    }
    
    @Test
    void assertGetSchemaTableAggregationReviser() {
        Optional<ShardingSchemaTableAggregationReviser> schemaTableAggregationReviser = reviseEntry.getSchemaTableAggregationReviser(new ConfigurationProperties(null));
        assertTrue(schemaTableAggregationReviser.isPresent());
        assertThat(schemaTableAggregationReviser.get().getClass(), is(ShardingSchemaTableAggregationReviser.class));
    }
    
    private static ShardingRule createShardingRule() {
        ShardingRuleConfiguration ruleConfig = createShardingRuleConfiguration();
        InstanceContext instanceContext = mock(InstanceContext.class);
        when(instanceContext.getWorkerId()).thenReturn(0);
        return new ShardingRule(ruleConfig, Collections.singletonMap("ds", new MockedDataSource()), instanceContext);
    }
    
    private static ShardingRuleConfiguration createShardingRuleConfiguration() {
        ShardingRuleConfiguration result = new ShardingRuleConfiguration();
        ShardingTableRuleConfiguration tableRuleConfig = new ShardingTableRuleConfiguration("t_order", "ds.t_order${0..1}");
        result.getTables().add(tableRuleConfig);
        return result;
    }
}
