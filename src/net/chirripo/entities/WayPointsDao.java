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

import net.chirripo.entities.WayPoints;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table WAY_POINTS.
*/
public class WayPointsDao extends AbstractDao<WayPoints, Long> {

    public static final String TABLENAME = "WAY_POINTS";

    /**
     * Properties of entity WayPoints.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Lat = new Property(1, double.class, "lat", false, "LAT");
        public final static Property Lng = new Property(2, double.class, "lng", false, "LNG");
        public final static Property Distance = new Property(3, double.class, "distance", false, "DISTANCE");
        public final static Property RouteId = new Property(4, long.class, "routeId", false, "ROUTE_ID");
        public final static Property Count = new Property(5, long.class, "count", false, "COUNT");
    };

    private Query<WayPoints> routes_Fk_route_waypointsQuery;
    private Query<WayPoints> routes_Fk_runRoute_waypointsQuery;

    public WayPointsDao(DaoConfig config) {
        super(config);
    }
    
    public WayPointsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'WAY_POINTS' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'LAT' REAL NOT NULL ," + // 1: lat
                "'LNG' REAL NOT NULL ," + // 2: lng
                "'DISTANCE' REAL NOT NULL ," + // 3: distance
                "'ROUTE_ID' INTEGER NOT NULL ," + // 4: routeId
                "'COUNT' INTEGER NOT NULL );"); // 5: count
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'WAY_POINTS'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, WayPoints entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindDouble(2, entity.getLat());
        stmt.bindDouble(3, entity.getLng());
        stmt.bindDouble(4, entity.getDistance());
        stmt.bindLong(5, entity.getRouteId());
        stmt.bindLong(6, entity.getCount());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public WayPoints readEntity(Cursor cursor, int offset) {
        WayPoints entity = new WayPoints( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getDouble(offset + 1), // lat
            cursor.getDouble(offset + 2), // lng
            cursor.getDouble(offset + 3), // distance
            cursor.getLong(offset + 4), // routeId
            cursor.getLong(offset + 5) // count
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, WayPoints entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLat(cursor.getDouble(offset + 1));
        entity.setLng(cursor.getDouble(offset + 2));
        entity.setDistance(cursor.getDouble(offset + 3));
        entity.setRouteId(cursor.getLong(offset + 4));
        entity.setCount(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(WayPoints entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(WayPoints entity) {
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
    
    /** Internal query to resolve the "fk_route_waypoints" to-many relationship of Routes. */
    public List<WayPoints> _queryRoutes_Fk_route_waypoints(long routeId) {
        synchronized (this) {
            if (routes_Fk_route_waypointsQuery == null) {
                QueryBuilder<WayPoints> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.RouteId.eq(null));
                routes_Fk_route_waypointsQuery = queryBuilder.build();
            }
        }
        Query<WayPoints> query = routes_Fk_route_waypointsQuery.forCurrentThread();
        query.setParameter(0, routeId);
        return query.list();
    }

    /** Internal query to resolve the "fk_runRoute_waypoints" to-many relationship of Routes. */
    public List<WayPoints> _queryRoutes_Fk_runRoute_waypoints(long count) {
        synchronized (this) {
            if (routes_Fk_runRoute_waypointsQuery == null) {
                QueryBuilder<WayPoints> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Count.eq(null));
                routes_Fk_runRoute_waypointsQuery = queryBuilder.build();
            }
        }
        Query<WayPoints> query = routes_Fk_runRoute_waypointsQuery.forCurrentThread();
        query.setParameter(0, count);
        return query.list();
    }

}
