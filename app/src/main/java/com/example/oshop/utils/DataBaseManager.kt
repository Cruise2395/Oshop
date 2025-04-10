class DataBaseManager(context: Context) : SQLiteOpenHelper(context, "ProductDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE Category (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
            );
        """)
        db.execSQL("""
            CREATE TABLE Product (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                categoryId INTEGER,
                checked INTEGER DEFAULT 0,
                FOREIGN KEY (categoryId) REFERENCES Category(id)
            );
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Product")
        db.execSQL("DROP TABLE IF EXISTS Category")
        onCreate(db)
    }
}
