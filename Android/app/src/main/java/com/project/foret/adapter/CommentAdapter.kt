package com.project.foret.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.foret.R
import com.project.foret.model.Comment

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.findViewById<TextView>(R.id.tvCommentWriter).text = comment.member.name
            holder.itemView.findViewById<TextView>(R.id.tvCommentContent).text = comment.content
            holder.itemView.findViewById<TextView>(R.id.tvCommentRegDate).text =
                comment.reg_date.substring(0, comment.reg_date.indexOf("T"))
            setOnClickListener {
                onItemClickListener?.let { it(comment) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Comment) -> Unit)? = null

    fun setOnItemClickListener(listener: (Comment) -> Unit) {
        onItemClickListener = listener
    }
}