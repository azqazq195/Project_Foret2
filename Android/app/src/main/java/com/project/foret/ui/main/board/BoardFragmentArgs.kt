package com.project.foret.ui.main.board

import android.os.Bundle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Long
import kotlin.jvm.JvmStatic

data class BoardFragmentArgs(
  val boardId: Long
) : NavArgs {
  fun toBundle(): Bundle {
    val result = Bundle()
    result.putLong("boardId", this.boardId)
    return result
  }

  companion object {
    @JvmStatic
    fun fromBundle(bundle: Bundle): BoardFragmentArgs {
      bundle.setClassLoader(BoardFragmentArgs::class.java.classLoader)
      val __boardId : Long
      if (bundle.containsKey("boardId")) {
        __boardId = bundle.getLong("boardId")
      } else {
        throw IllegalArgumentException("Required argument \"boardId\" is missing and does not have an android:defaultValue")
      }
      return BoardFragmentArgs(__boardId)
    }
  }
}
