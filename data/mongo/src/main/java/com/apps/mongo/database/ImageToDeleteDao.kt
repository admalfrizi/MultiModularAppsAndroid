package com.apps.mongo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apps.mongo.database.entity.ImageToDelete
import com.apps.util.Constants.IMAGES_TO_DELETE_TABLE

@Dao
interface ImageToDeleteDao {
    @Query("SELECT * FROM $IMAGES_TO_DELETE_TABLE ORDER BY id ASC")
    suspend fun getAllImages(): List<ImageToDelete>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageToDelete(imageToUpload: ImageToDelete)

    @Query("DELETE FROM $IMAGES_TO_DELETE_TABLE WHERE id=:imageId")
    suspend fun cleanupImage(imageId: Int)
}