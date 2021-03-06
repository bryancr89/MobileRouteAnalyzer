package net.chirripo.entities;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import net.chirripo.entities.RunRoutes;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table RUN_ROUTES.
*/
public class RunRoutesDao extends AbstractDao<RunRoutes, Long> {

    public static final String TABLENAME = "RUN_ROUTES";

    /**
     * Properties of entity RunRoutes.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Distance = new Property(1, double.class, "distance", false, "DISTANCE");
        public final static Property Duration = new Property(2, double.class, "duration", false, "DURATION");
        public final static Property Count = new Property(3, int.class, "count", false, "COUNT");
        public final static Property Rundate = new Property(4, java.util.Date.class, "rundate", false, "RUNDATE");
        public final static Property RouteId = new Property(5, long.class, "routeId", false, "ROUTE_ID");
    };

    private Query<RunRoutes> routes_Fk_route_runRoutesQuery;

    public RunRoutesDao(DaoConfig config) {
        super(config);
    }
    
    public RunRoutesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'RUN_ROUTES' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'DISTANCE' REAL NOT NULL ," + // 1: distance
                "'DURATION' REAL NOT NULL ," + // 2: duration
                "'COUNT' INTEGER NOT NULL ," + // 3: count
                "'RUNDATE' INTEGER," + // 4: rundate
                "'ROUTE_ID' INTEGER NOT NULL );"); // 5: routeId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'RUN_ROUTES'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RunRoutes entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindDouble(2, entity.getDistance());
        stmt.bindDouble(3, entity.getDuration());
        stmt.bindLong(4, entity.getCount());
 
        java.util.Date rundate = entity.getRundate();
        if (rundate != null) {
            stmt.bindLong(5, rundate.getTime());
        }
        stmt.bindLong(6, entity.getRouteId());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public RunRoutes readEntity(Cursor cursor, int offset) {
        RunRoutes entity = new RunRoutes( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getDouble(offset + 1), // distance
            cursor.getDouble(offset + 2), // duration
            cursor.getInt(offset + 3), // count
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // rundate
            cursor.getLong(offset + 5) // routeId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RunRoutes entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDistance(cursor.getDouble(offset + 1));
        entity.setDuration(cursor.getDouble(offset + 2));
        entity.setCount(cursor.getInt(offset + 3));
        entity.setRundate(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setRouteId(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RunRoutes entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(RunRoutes entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "fk_route_runRoutes" to-many relationship of Routes. */
    public List<RunRoutes> _queryRoutes_Fk_route_runRoutes(long routeId) {
        synchronized (this) {
            if (routes_Fk_route_runRoutesQuery == null) {
                QueryBuilder<RunRoutes> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.RouteId.eq(null));
                routes_Fk_route_runRoutesQuery = queryBuilder.build();
            }
        }
        Query<RunRoutes> query = routes_Fk_route_runRoutesQuery.forCurrentThread();
        query.setParameter(0, routeId);
        return query.list();
    }

}
