package org.voltdb.calciteadapter.rel;

import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rel.RelDistribution;
import org.apache.calcite.rel.RelReferentialConstraint;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.Schema.TableType;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Table;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.util.ImmutableBitSet;
import org.voltdb.VoltType;
import org.voltdb.catalog.Column;
import org.voltdb.catalog.MaterializedViewInfo;
import org.voltdb.utils.CatalogUtil;

import java.util.ArrayList;
import java.util.List;

import static org.voltdb.calciteadapter.rel.VoltDBTable.toRelDataType;

public class VoltDBMatViewInfo implements Table {
    private final MaterializedViewInfo m_catMVInfo;
    VoltDBMatViewInfo(MaterializedViewInfo info) {
        assert(info != null) : "null voltdb.catalog.MaterializedViewInfo";
        m_catMVInfo = info;
    }
    MaterializedViewInfo getCatMVInfo() {
        return m_catMVInfo;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return new RelDataTypeFactory.Builder(typeFactory) {{
            CatalogUtil
                    .getSortedCatalogItems(getCatMVInfo().getGroupbycols(), "index")
                    .forEach(catGbyColumnRef -> {
                        final Column col = catGbyColumnRef.getColumn();
                        add(col.getName(), typeFactory.createTypeWithNullability(toRelDataType(typeFactory,
                                VoltType.get((byte)col.getType()),
                                col.getSize()),
                                col.getNullable()));
                    });
        }}.build();
    }

    @Override
    public Statistic getStatistic() {
        return new Statistic() {
            @Override
            public Double getRowCount() {
                return null;
            }
            @Override
            public boolean isKey(ImmutableBitSet columns) {
                return false;
            }
            @Override
            public List<RelCollation> getCollations() {
                return new ArrayList<>();
            }
            @Override
            public RelDistribution getDistribution() {
                return null;        // TODO:   /** Returns the distribution of the data in this table. */
            }
            @Override
            public List<RelReferentialConstraint> getReferentialConstraints() {
                return null;
            }
        };
    }

    @Override
    public TableType getJdbcTableType() {
        return TableType.MATERIALIZED_VIEW;
    }

    @Override
    public boolean isRolledUp(String column) {
        return false;
    }

    @Override
    public boolean rolledUpColumnValidInsideAgg(String column, SqlCall call,
                                                SqlNode parent, CalciteConnectionConfig config) {
        return false;
    }
}
