package com.project.foret.ui.main.annoymousForum

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.project.foret.R
import kotlin.Int
import kotlin.Long

class AnonymousForumFragmentDirections private constructor() {
  private data class ActionAnonymousForumFragmentToBoardFragment(
    val boardId: Long
  ) : NavDirections {
    override fun getActionId(): Int = R.id.action_anonymousForumFragment_to_boardFragment

    override fun getArguments(): Bundle {
      val result = Bundle()
      result.putLong("boardId", this.boardId)
      return result
    }
  }

  companion object {
    fun actionAnonymousForumFragmentToBoardFragment(boardId: Long): NavDirections =
        ActionAnonymousForumFragmentToBoardFragment(boardId)

    fun actionAnonymousForumFragmentToCreateBoardActivity(): NavDirections =
        ActionOnlyNavDirections(R.id.action_anonymousForumFragment_to_createBoardActivity)
  }
}
