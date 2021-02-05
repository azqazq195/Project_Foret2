package com.project.foret.ui.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.project.foret.R
import com.project.foret.model.SignUp
import java.util.*

class SignUp2 : AppCompatActivity() {

    val DELAY = 800L
    private var moveNext = 0
    val formList =
        listOf(
            SignUp(0, "이름", "이름", null, false),
            SignUp(1, "닉네임", "닉네임", null, false),
            SignUp(2, "생일", "생일", null, false),
            SignUp(3, "이메일", "이메일", null, false),
            SignUp(4, "비번1", "비번1", null, false),
            SignUp(5, "비번2", "비번2", null, false)
        )
    private val data = arrayOf("", "", "", "", "", "")

    lateinit var signUpAdapter: SignUpAdapter

    lateinit var vpSignUp: ViewPager2
    lateinit var btnNext: Button
    lateinit var tvExplain: TextView
    private var animShow: Animation? = null

    var nameValidation = Regex("^[A-z|가-힣]([A-z|가-힣]*)$")
    var nickValidation = Regex("^[A-z|가-힣|0-9]([A-z|가-힣|0-9]*)$")
    var emailValidation = Regex("^[A-z|0-9]([A-z|0-9]*)(@)([A-z]*)(\\.)([a-zA-Z]){2,3}$")

    //    String pwValidation = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{6,12}$";
    var passwordValidation =
        Regex("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*])[A-Za-z[0-9]$@$!%*#?&].{5,12}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        setFindViewById()
        setUpRecyclerView()
        setOnClickListener()

        animShow = AnimationUtils.loadAnimation(this, R.anim.sign_up)
        tvExplain.text = "이름을 입력해 주세요."
    }

    private fun setOnClickListener() {
        btnNext.setOnClickListener {

        }
    }

    private fun setFindViewById() {
        vpSignUp = findViewById(R.id.vpSignUp)
        tvExplain = findViewById(R.id.tvExplain)
        btnNext = findViewById(R.id.btnNext)
    }

    private fun setUpRecyclerView() {
        signUpAdapter = SignUpAdapter()
        vpSignUp.apply {
            adapter = signUpAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
    }

    inner class SignUpAdapter :
        RecyclerView.Adapter<SignUpAdapter.SignUpViewHolder>() {
        inner class SignUpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignUpViewHolder {
            return SignUpViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_sign_up,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: SignUpViewHolder, position: Int) {
            holder.itemView.apply {
                holder.itemView.findViewById<TextView>(R.id.tvError).text =
                    formList[position].errorMessage
                holder.itemView.findViewById<EditText>(R.id.etInput).hint =
                    formList[position].hintMessage
                holder.itemView.findViewById<EditText>(R.id.etInput)
                    .setText(formList[position].value)
                if (!formList[position].check) {
                    holder.itemView.findViewById<ImageView>(R.id.ivCheck).visibility =
                        View.GONE
                }

                holder.itemView.findViewById<EditText>(R.id.etInput)
                    .addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            holder.itemView.findViewById<ImageView>(R.id.ivCheck).visibility =
                                View.INVISIBLE
                            formList[position].value =
                                holder.itemView.findViewById<EditText>(R.id.etInput).text.toString()
                            formList[position].check =
                                false
                        }

                        var timer = Timer()
                        override fun afterTextChanged(s: Editable) {
                            timer.cancel()
                            timer = Timer()
                            timer.schedule(object : TimerTask() {
                                override fun run() {
                                    runOnUiThread {
                                        checkValue(this@apply, position, s)
                                        setBtnView()
                                    }
                                }
                            }, DELAY)
                        }
                    })
            }
        }

        override fun getItemCount(): Int = formList.size

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }
    }

    private fun checkValue(view: View, position: Int, s: Editable) {
        val condition = when (position) {
            0 -> s.matches(nameValidation) && s.length > 1
            1 -> s.matches(nickValidation) && s.length > 1
            2 -> s.length == 8
            // 3 -> s.matches(emailValidation) && s.length > 1
            3 -> s.length > 1
            4 -> s.matches(passwordValidation) && s.length > 1
            // 5 -> etPassword1.text.toString() == s.toString()
            5 -> s.length > 1
            else -> false
        }
        if (condition) {
            formList[position].check = true
            view.findViewById<ImageView>(R.id.ivCheck).visibility = View.VISIBLE
            if (moveNext < 5) {
                vpSignUp.setCurrentItem(++vpSignUp.currentItem, true)
                moveNext++
                setExplainTextView()
            } else {
                tvExplain.text = "다음으로 넘어가기"
                hideKeyboard()
            }
        }
    }

    private fun setBtnView() {
        for (i in formList) {
            if (!i.check) {
                btnNext.visibility = View.INVISIBLE
                return
            }
        }
        btnNext.visibility = View.VISIBLE
    }

    private fun setExplainTextView() {
        when(moveNext){
            1 -> tvExplain.text = "닉네임을 입력해 주세요."
            2 -> tvExplain.text = "생일을 입력해 주세요."
            3 -> tvExplain.text = "이메일을 입력해 주세요."
            4 -> tvExplain.text = "비밀번호를 입력해 주세요."
            5 -> tvExplain.text = "비밀번호를 확인해 주세요."
        }
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }
}