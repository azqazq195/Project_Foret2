//package com.project.foret.adapter
//
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.project.foret.R
//import com.project.foret.model.SignUp
//
//class SignUpAdapter(private val signUp: List<SignUp>) :
//    RecyclerView.Adapter<SignUpAdapter.SignUpViewHolder>() {
//    inner class SignUpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignUpViewHolder {
//        return SignUpViewHolder(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.item_sign_up,
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: SignUpViewHolder, position: Int) {
//        holder.itemView.apply {
//            holder.itemView.findViewById<TextView>(R.id.tvError).text = signUp[position].errorMessage
//            holder.itemView.findViewById<EditText>(R.id.etHint).hint = position.toString()
//            holder.itemView.findViewById<TextView>(R.id.etHint)
//                .addTextChangedListener( object : TextWatcher {
//                    override fun beforeTextChanged(
//                        s: CharSequence?,
//                        start: Int,
//                        count: Int,
//                        after: Int
//                    ) {
//                    }
//
//                    override fun onTextChanged(
//                        s: CharSequence?,
//                        start: Int,
//                        before: Int,
//                        count: Int
//                    ) {
//                    }
//
//                    override fun afterTextChanged(s: Editable?) {
//                        when(position){
//                            0 -> {
//
//                            }
//                        }
//                    }
//
//                })
//        }
//    }
//
//    override fun getItemCount(): Int = signUp.size
//
//}