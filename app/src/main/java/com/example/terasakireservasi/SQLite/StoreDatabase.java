package com.example.terasakireservasi.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.terasakireservasi.Model.Cart;
import com.example.terasakireservasi.Model.Data;
import com.example.terasakireservasi.Model.ModelChart;
import com.example.terasakireservasi.Model.ModelItem;
import com.example.terasakireservasi.Model.ModelProduct;
import com.example.terasakireservasi.Model.Table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreDatabase {

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_PRICE = "price";
    public static final String KEY_STATUS = "status";
    public static final String KEY_QTY = "qty";

    //table booking

    public static final String ID = "id";
    public static final String SEAT_NO = "seat";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String PRICE = "price";




    private static final String TAG = "CoffeHouse";
    private static DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "TerasakiCoffe";
    private static final String SHOP_TABLE = "shop";
    private static final String BOOKING_TABLE = "book";
    private static final int DATABASE_VERSION = 1;

    private final Context mContext;

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + SHOP_TABLE + " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_NAME + " TEXT,"+
            KEY_PRICE + " INTEGER," +
            KEY_QTY + " TEXT," +
            KEY_IMAGE + " TEXT," +
            KEY_STATUS + " TEXT" +");";

    private static final String CREATE_BOOKING = "CREATE TABLE IF NOT EXISTS "
            + BOOKING_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SEAT_NO + " TEXT,"+
            DATE + " TEXT," +
            TIME + " TEXT," +
            PRICE + " INTEGER" +");";


    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
            db.execSQL(CREATE_BOOKING);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+SHOP_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+BOOKING_TABLE);
            onCreate(db);

        }
    }

    public StoreDatabase(Context context){
        this.mContext = context;
    }

    public StoreDatabase open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getReadableDatabase();
        return this;
    }

    public void close(){
        if (mDbHelper != null){
            mDbHelper.close();
        }
    }

    public boolean addToCart (String name, Integer price,String qty, String img,  String id){
        mDb = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_PRICE, price);
        contentValues.put(KEY_QTY, qty);
        contentValues.put(KEY_IMAGE, img);
        contentValues.put(KEY_STATUS, id);
        mDb.insert(SHOP_TABLE,null,contentValues);
        mDb.close();
        return true;
    }

    public boolean addBookTable(String seatNo, String date, String time , Integer price){
        mDb = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SEAT_NO, seatNo);
        contentValues.put(DATE, date);
        contentValues.put(TIME, time);
        contentValues.put(PRICE,price);
        mDb.insert(BOOKING_TABLE, null, contentValues);
        Log.d("ADD BOOK TABLE", contentValues.toString());
        mDb.close();
        return true;
    }

//
//    public long createItem(String name, String description, int price, String status) {
//
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_NAME, name);
//        initialValues.put(KEY_IMAGE, description);
//        initialValues.put(KEY_PRICE, price);
//        initialValues.put(KEY_STATUS, status);
//
//        return mDb.insert(SHOP_TABLE, null, initialValues);
//    }

    public void deleteAllItems() {
        mDb = mDbHelper.getReadableDatabase();
        mDb.execSQL("DELETE FROM "+SHOP_TABLE);

    }

    public void deleteAllTable(){
        mDb = mDbHelper.getReadableDatabase();
        mDb.execSQL("DELETE FROM "+BOOKING_TABLE);
    }

    public int getCartItemsRowCount(int type){
        mDb = mDbHelper.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(mDb, SHOP_TABLE, "status= ? ", new String[]{Integer.toString(type)});
    }

    public int getTotalBookTable(){
        String query = "SELECT * FROM "+BOOKING_TABLE;
        mDb = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(query, null);
        int result = cursor.getCount();
        cursor.close();
        return result;
    }


    public int getTotalItemsCount() {
        String countQuery = "SELECT  * FROM " + SHOP_TABLE;
        mDb = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int getAmount() {
        mDb = mDbHelper.getReadableDatabase();

        String query = "SELECT SUM("+KEY_PRICE+") FROM "+ SHOP_TABLE;
        Cursor cursor = mDb.rawQuery(query,null);
      //  Cursor cursor = mDb.rawQuery("SELECT SUM(" + KEY_PRICE + ") FROM " + SHOP_TABLE,null );
        int total = 0;
        if(cursor.moveToFirst()) {
            total = cursor.getColumnCount();
        }
        return total;
    }

    public List<ModelProduct> getProductItem() {

        mDb = mDbHelper.getReadableDatabase();
        List<ModelProduct> list = new ArrayList<>();
        String query = "SELECT *,SUM("+KEY_PRICE+") FROM "+ SHOP_TABLE;
        ModelProduct product = new ModelProduct();
        Cursor cursor = mDb.rawQuery(query,null);

        while (cursor.moveToNext()){
            product.setNameItem(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            product.setIdItem(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
            product.setImageItem(cursor.getString(cursor.getColumnIndex(KEY_IMAGE)));
            product.setPriceItem(cursor.getString(cursor.getColumnIndex(KEY_PRICE)));
            product.setQtyItem(cursor.getString(cursor.getColumnIndex(KEY_QTY)));
            list.add(product);
        }
        return list;
    }
    public ArrayList<ArrayList<Object>> getAllBookTable(){
        mDb = mDbHelper.getReadableDatabase();
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

        Cursor cursor = null;

        try{
            cursor = mDb.query(
                    BOOKING_TABLE,
                    new String[]{ID, SEAT_NO, DATE, TIME, PRICE},
                    null,null, null, null, null);
            cursor.moveToFirst();

            if (!cursor.isAfterLast()){
                do{
                    ArrayList<Object> dataList = new ArrayList<Object>();

                    dataList.add(cursor.getInt(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getString(2));
                    dataList.add(cursor.getString(3));
                    dataList.add(cursor.getString(4));
                    dataArrays.add(dataList);
                }

                while (cursor.moveToNext());
            }
            cursor.close();
        }catch (android.database.SQLException e){
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return dataArrays;
    }

    public ArrayList<ArrayList<Object>> getAllData(){
        mDb = mDbHelper.getReadableDatabase();
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

        Cursor cursor = null;

        try{
            cursor = mDb.query(
                    SHOP_TABLE,
                    new String[]{KEY_ID, KEY_NAME, KEY_IMAGE, KEY_STATUS, KEY_PRICE, KEY_QTY},
                    null,null, null, null, null);
            cursor.moveToFirst();

            if (!cursor.isAfterLast()){
                do{
                    ArrayList<Object> dataList = new ArrayList<Object>();

                    dataList.add(cursor.getInt(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getString(2));
                    dataList.add(cursor.getString(3));
                    dataList.add(cursor.getInt(4));
                    dataList.add(cursor.getString(5));

                    dataArrays.add(dataList);
                }

                while (cursor.moveToNext());
            }
            cursor.close();
        }catch (android.database.SQLException e){
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return dataArrays;
    }

    public ArrayList<Cart> getCart(){
        mDbHelper.getReadableDatabase();
        ArrayList<Cart> cartList = new ArrayList<>();
        String sqllite = "SELECT * FROM "+SHOP_TABLE;
        Cursor cursor = mDb.rawQuery(sqllite,null);
        while (cursor.moveToNext()){
            String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            String price = cursor.getString(cursor.getColumnIndex(KEY_PRICE));
            String qty = cursor.getString(cursor.getColumnIndex(KEY_QTY));
            Cart cart = new Cart(name, status, qty, price);
            cartList.add(cart);
        }
        return cartList;
    }

//    public ArrayList<Table> getTable(){
//        mDbHelper.getReadableDatabase();
//        ArrayList<Table> tableList = new ArrayList<>();
//        String sql = "SELECT * FROM "+BOOKING_TABLE;
//        Cursor cursor = mDb.rawQuery(sql, null);
//        while (cursor.moveToNext()){
//            String id = cursor.getString(cursor.getColumnIndex(ID));
//            String seatNo = cursor.getString(cursor.getColumnIndex(SEAT_NO));
//            String date = cursor.getString(cursor.getColumnIndex(DATE));
//            String time = cursor.getString(cursor.getColumnIndex(TIME));
//            int price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRICE)));
//            Table table = new Table(id, seatNo, date, time, price);
//            tableList.add(table);
//        }
//        return tableList;
//    }

    public ArrayList<HashMap<String, String>> GetUsers(){
        mDb = mDbHelper.getReadableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM "+ SHOP_TABLE;
        Cursor cursor = mDb.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("name","Nama Pesanan : "+cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.put("price","Harga Item : "+cursor.getString(cursor.getColumnIndex(KEY_PRICE)));
            user.put("status",cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
            user.put("qty","Jumlah Pesanan :"+cursor.getString(cursor.getColumnIndex(KEY_QTY)));
            userList.add(user);
        }
        return  userList;
    }

    public Cursor fetchAllItems() {
        Cursor mCursor = mDb.query(SHOP_TABLE, new String[] {KEY_NAME, KEY_PRICE,KEY_QTY, KEY_STATUS},
                KEY_NAME ,null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

//    public void insertMyShopItems() {
//        createItem("San Andreas (Special Edition DVD)", "Think of this movie as every disaster movie rolled into one. I enjoyed San Andreas a lot and they certainly managed to cram almost every survival scenario into one movie.\n" +
//                "\n" +
//                "When the movie started I thought it would be insanely cheesy with some of the lines Dwayne Johnson was saying, but luckily it got much better. Don't get me wrong, there were certainly cheesy parts (certain romances, and cliches) but when you see the first 5 minutes you'll know what I mean about expecting it to be way cheesier.", 999, "0");
//        createItem("Spectre 007 (Blu-ray)", "The movie opens with a terrific action sequence in Mexico, set on the Day of the Dead. Bond is off on a private mission, while MI-6, in the fallout from \"Skyfall\", is facing termination. With help from Moneypenny (Naomie Harris) and Q (Ben Whishaw), Bond will doggedly pursue a trail of clues to a young woman who may have some of his answers. Together, they will find and face an old acquaintance who is the head of a very evil and very powerful organization", 1989, "0");
//        createItem("The Martian [Blu-ray]", "Matt Damon's Everyman quality and wry sense of humor makes him the perfect actor to portray Mark Watney, astronaut, in Ridley Scott's \"The Martian,\" written by Drew Goddard, based on the book by Andy Weir. When a violent storm forces the Hermes crew, led by Commander Melissa Lewis (a steely Jessica Chastain), to abort the Ares III mission to Mars, the crew reluctantly leaves without Mark Watney, who was injured and presumed dead. Somehow, Watney survives, and starts keeping a log of his experiences on the Red Planet. He is a realist. It could take years for a rescue team to retrieve him, so he gets down to business: rationing and growing food, making water, repairing damaged equipment, and establishing communication with Mission Control.", 1799, "0");
//        createItem("Hitman: Agent 47 [Blu-ray]", "Less talk, more action and finally sexy! That's the new Hitman! While critics beat the movie down with their \"professional\" reviews I applaud it since finally, after a long long time (see Riddick, 2013) there's an action movie which focuses more on the action and less on empty mono/dialogues and talks about who's the bigger baddie and who's gonna rule the world (see Gotham, 2014; Jupiter Ascending, 2015; and many, many others). And so while \"movie experts\" compete to find the most nonsense in the movie I will gladly continue to take delight in this stunning glorification of violence with breathtaking photography and luxurious futuristic shots.", 1799, "0");
//        createItem("The Intern (Blu-ray + DVD + ULTRAVIOLET)", "There's something delightfully old-fashioned about Nancy Meyers' The Intern that makes it more entertaining than it has any right to be. It's best exemplified in her lead actor Robert De Niro, one of the kings of old school cool, and the character he plays, Ben, the last of a dying breed of real gentlemen. In Meyers' hands the story of a 70-year-old widower venturing into the fast-paced, energetic tech world is every bit as breezy as one might expect, but she still finds a little room to hit on a few salient points that make it more engaging than her usual trifles.", 2099, "0");
//        createItem("Star Wars: The Complete Saga (Episodes I-VI) [Blu-ray]", "Simply sensational!", 8999, "0");
//    }
}
