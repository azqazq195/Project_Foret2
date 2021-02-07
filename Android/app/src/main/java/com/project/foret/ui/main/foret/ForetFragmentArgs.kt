package com.project.foret.ui.main.foret

import android.os.Bundle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Long
import kotlin.jvm.JvmStatic

data class ForetFragmentArgs(
  val foretId: Long
) : NavArgs {
  fun toBundle(): Bundle {
    val result = Bundle()
    result.putLong("foretId", this.foretId)
    return result
  }

  companion object {
    @JvmStatic
    fun fromBundle(bundle: Bundle): ForetFragmentArgs {
      bundle.setClassLoader(ForetFragmentArgs::class.java.classLoader)
      val __foretId : Long
      if (bundle.containsKey("foretId")) {
        __foretId = bundle.getLong("foretId")
      } else {
        throw IllegalArgumentException("Required argument \"foretId\" is missing and does not have an android:defaultValue")
      }
      return ForetFragmentArgs(__foretId)
    }
  }
}
