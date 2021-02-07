package com.project.foret.ui.main.foret

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.project.foret.R
import kotlin.Int
import kotlin.Long

class ForetFragmentDirections private constructor() {
  private data class ActionForetFragmentToBoardFragment(
    val boardId: Long
  ) : NavDirections {
    override fun getActionId(): Int = R.id.action_foretFragment_to_boardFragment

    override fun getArguments(): Bundle {
      val result = Bundle()
      result.putLong("boardId", this.boardId)
      return result
    }
  }

  companion object {
    fun actionForetFragmentToBoardFragment(boardId: Long): NavDirections =
        ActionForetFragmentToBoardFragment(boardId)

    fun actionForetFragmentToCreateBoardActivity(): NavDirections =
        ActionOnlyNavDirections(R.id.action_foretFragment_to_createBoardActivity)
  }
}
