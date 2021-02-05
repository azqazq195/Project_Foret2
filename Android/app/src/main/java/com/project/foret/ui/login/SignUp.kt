package com.project.foret.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.project.foret.R
import com.project.foret.util.snackbar
import java.util.*

class SignUp : AppCompatActivity() {
    private val DELAY = 700L

    lateinit var tvName: TextView
    lateinit var tvNickName: TextView
    lateinit var tvBirth: TextView
    lateinit var tvEmail: TextView
    lateinit var tvPassword: TextView

    lateinit var etName: EditText
    lateinit var etNickName: EditText
    lateinit var etBirth: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword1: EditText
    lateinit var etPassword2: EditText

    lateinit var ivCheckName: ImageView
    lateinit var ivCheckNickName: ImageView
    lateinit var ivCheckBirth: ImageView
    lateinit var ivCheckEmail: ImageView
    lateinit var ivCheckPassword1: ImageView
    lateinit var ivCheckPassword2: ImageView

    lateinit var layoutName: LinearLayout
    lateinit var layoutNickName: LinearLayout
    lateinit var layoutBirth: LinearLayout
    lateinit var layoutEmail: LinearLayout
    lateinit var layoutPassword: LinearLayout
    lateinit var layoutNext: LinearLayout

    lateinit var layoutRoot: ConstraintLayout
    lateinit var btnNext: Button
    lateinit var tvExplain: TextView

//    lateinit var textViews: Array<TextView>
//    lateinit var editTexts: Array<EditText>
//    lateinit var ivChecks: Array<ImageView>
//    lateinit var layouts: Array<LinearLayout>

    private var animShow: Animation? = null

    var nameValidation = Regex("^[A-z|가-힣]([A-z|가-힣]*)$")
    var nickValidation = Regex("^[A-z|가-힣|0-9]([A-z|가-힣|0-9]*)$")
    var emailValidation = Regex("^[A-z|0-9]([A-z|0-9]*)(@)([A-z]*)(\\.)([a-zA-Z]){2,3}$")

    //    String pwValidation = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{6,12}$";
    var passwordValidation =
        Regex("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*])[A-Za-z[0-9]$@$!%*#?&].{5,12}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setFindViewById()
        setTextChangedListener()
        setOnClickListener()

        animShow = AnimationUtils.loadAnimation(this, R.anim.sign_up)

        tvExplain.text = "이름을 입력해 주세요."
    }

    private fun setFindViewById() {
        tvName = findViewById(R.id.tvName)
        tvNickName = findViewById(R.id.tvNickName)
        tvBirth = findViewById(R.id.tvBirth)
        tvEmail = findViewById(R.id.tvEmail)
        tvPassword = findViewById(R.id.tvPassword)

        etName = findViewById(R.id.etName)
        etNickName = findViewById(R.id.etNickName)
        etBirth = findViewById(R.id.etBirth)
        etEmail = findViewById(R.id.etEmail)
        etPassword1 = findViewById(R.id.etPassword1)
        etPassword2 = findViewById(R.id.etPassword2)

        ivCheckName = findViewById(R.id.ivCheckName)
        ivCheckNickName = findViewById(R.id.ivCheckNickName)
        ivCheckBirth = findViewById(R.id.ivCheckBirth)
        ivCheckEmail = findViewById(R.id.ivCheckEmail)
        ivCheckPassword1 = findViewById(R.id.ivCheckPassword1)
        ivCheckPassword2 = findViewById(R.id.ivCheckPassword2)

        layoutName = findViewById(R.id.layoutName)
        layoutNickName = findViewById(R.id.layoutNickName)
        layoutBirth = findViewById(R.id.layoutBirth)
        layoutEmail = findViewById(R.id.layoutEmail)
        layoutPassword = findViewById(R.id.layoutPassword)
        layoutNext = findViewById(R.id.layoutNext)

        layoutRoot = findViewById(R.id.layoutRoot)
        btnNext = findViewById(R.id.btnNext)
        tvExplain = findViewById(R.id.tvExplain)

//        textViews = arrayOf(
//            findViewById(R.id.tvName),
//            findViewById(R.id.tvNickName),
//            findViewById(R.id.tvBirth),
//            findViewById(R.id.tvEmail),
//            findViewById(R.id.tvPassword)
//        )
//
//        editTexts = arrayOf(
//            findViewById(R.id.etName),
//            findViewById(R.id.etNickName),
//            findViewById(R.id.etBirth),
//            findViewById(R.id.etEmail),
//            findViewById(R.id.etPassword1),
//            findViewById(R.id.etPassword2)
//        )
//
//        ivChecks = arrayOf(
//            findViewById(R.id.ivCheckName),
//            findViewById(R.id.ivCheckNickName),
//            findViewById(R.id.ivCheckBirth),
//            findViewById(R.id.ivCheckEmail),
//            findViewById(R.id.ivCheckPassword1),
//            findViewById(R.id.ivCheckPassword2)
//        )
//
//        layouts = arrayOf(
//            findViewById(R.id.layoutName),
//            findViewById(R.id.layoutNickName),
//            findViewById(R.id.layoutBirth),
//            findViewById(R.id.layoutEmail),
//            findViewById(R.id.layoutPassword)
//        )
    }

    private fun setOnClickListener() {
        btnNext.setOnClickListener{
            layoutRoot.snackbar("다음!")
        }
    }

    private fun setTextChangedListener() {
        checkName()
        checkNickName()
        checkBirth()
        checkEmail()
        checkPassword1()
        checkPassword2()
    }

    private fun showNextContentWithAnim(nextLayout: LinearLayout) {
        if (nextLayout.visibility == View.INVISIBLE || nextLayout.visibility == View.GONE) {
            nextLayout.visibility = View.VISIBLE
            nextLayout.startAnimation(animShow)
        }
    }

    private fun changeExplainTextView(message: String) {
        tvExplain.text = message
        tvExplain.startAnimation(animShow)
    }

    private fun checkName() {
        etName.addTextChangedListener(object : TextWatcher {
            var timer = Timer()
            override fun afterTextChanged(s: Editable) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            if (s.matches(nameValidation) && s.length > 1) {
                                ivCheckName.visibility = View.VISIBLE
                                showNextContentWithAnim(layoutNickName)
                                changeExplainTextView("닉네임을 입력해 주세요.")
                                etNickName.requestFocus()
                            }
                        }
                    }
                }, DELAY)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkNickName() {
        etNickName.addTextChangedListener(object : TextWatcher {
            var timer = Timer()
            override fun afterTextChanged(s: Editable) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            if (s.matches(nickValidation) && s.length > 1) {
                                ivCheckNickName.visibility = View.VISIBLE
                                showNextContentWithAnim(layoutBirth)
                                etBirth.requestFocus()
                                changeExplainTextView("생일을 입력해 주세요.")
                            }
                        }
                    }
                }, DELAY)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkBirth() {
        etBirth.addTextChangedListener(object : TextWatcher {
            var timer = Timer()
            override fun afterTextChanged(s: Editable) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            if (s.length == 8) {
                                ivCheckBirth.visibility = View.VISIBLE
                                showNextContentWithAnim(layoutEmail)
                                etEmail.requestFocus()
                                tvExplain.text = "이메일을 입력해 주세요."
                            }
                        }
                    }
                }, DELAY)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkEmail() {
        etEmail.addTextChangedListener(object : TextWatcher {
            var timer = Timer()
            override fun afterTextChanged(s: Editable) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            // if (s.matches(emailValidation) && s.length > 1) {
                            if (s.length > 1) {
                                ivCheckEmail.visibility = View.VISIBLE
                                showNextContentWithAnim(layoutPassword)
                                etPassword1.requestFocus()
                                tvExplain.text = "비밀번호를 입력해 주세요."
                            }
                        }
                    }
                }, DELAY)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkPassword1() {
        etPassword1.addTextChangedListener(object : TextWatcher {
            var timer = Timer()
            override fun afterTextChanged(s: Editable) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            if (s.matches(passwordValidation) && s.length > 1) {
                                ivCheckPassword1.visibility = View.VISIBLE
                                etPassword2.requestFocus()
                                tvExplain.text = "비밀번호를 확인해 주세요."
                            }
                        }
                    }
                }, DELAY)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkPassword2() {
        etPassword2.addTextChangedListener(object : TextWatcher {
            var timer = Timer()
            override fun afterTextChanged(s: Editable) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            if (etPassword1.text.toString() == s.toString()) {
                                Log.e("TAG", "실행")
                                ivCheckPassword2.visibility = View.VISIBLE
                                showNextContentWithAnim(layoutNext)
                                btnNext.requestFocus()
                            }
                        }
                    }
                }, DELAY)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

}