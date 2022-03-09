package com.freak.quiz_app

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val margin:Int =16
val Int.pixels: Int
    get()=(this * Resources.getSystem().displayMetrics.density).toInt()



class MainActivity : AppCompatActivity() {
    private var questions: MutableList<Question> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupQuestion()
        setupQuiz()
        setupSubmitButton()


    }


    private fun setupQuestion() {
        questions.add(
            Question(
                1,
                QuestionType.Text,
                "What is the capital of USA?",
                null,
                listOf("Washington DC")
            )
        )
        questions.add(
            Question(
                2,
                QuestionType.Checkbox,
                "Which of them are Unicorn Company?",
                listOf("Uber", "Tesla", "ChaiWala", "Kabade Vala"),
                listOf("Uber", "Tesla")
            )
        )
        questions.add(
            Question(
                3,
                QuestionType.Radio,
                "Which is the largest country Areawise?",
                listOf("India", "Russia", "USA", "Canada"),
                listOf("Russia")
            )
        )
        questions.add(
            Question(
                4,
                QuestionType.Checkbox,
                "Which of them are States ?",
                listOf("Gurugram", "Delhi", "M.P", "Bengaluru"),
                listOf("Delhi", "M.P")
            )
        )

    }

    private fun setupQuiz() {
        questions.forEachIndexed { index, element ->
            when (element.type) {
                QuestionType.Text -> {
                    setupTextQuestion(index, element)
                }
                QuestionType.Radio -> {
                    setupRadioQuestion(index, element)
                }
                QuestionType.Checkbox -> {
                    setupCheckBoxQuestion(index, element)
                }
            }
        }


    }

    private fun setupTextQuestion(counter: Int, q: Question) {
        val textView = getQuestionTextView(counter, q.qText)

        val editText = EditText(this)
        editText.id = q.id
        editText.isSingleLine = true /*Bcs Answer is single lined*/
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        Quiz_Container.addView(textView)
        Quiz_Container.addView(editText)


    }


    private fun setupRadioQuestion(counter: Int, q: Question) {
        val textView = getQuestionTextView(counter, q.qText)

        val radioGroup = RadioGroup(this)
        radioGroup.id = q.id
        radioGroup.orientation = RadioGroup.VERTICAL

        radioGroup.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )

        q.options?.forEachIndexed { index, element ->
            val radioButton = RadioButton(this)
            radioButton.text = element
            radioButton.id = (q.id.toString() + index.toString()).toInt()
            radioGroup.addView(radioButton)

        }
        Quiz_Container.addView(textView)
        Quiz_Container.addView(radioGroup)

    }


    private fun setupCheckBoxQuestion(counter: Int, q: Question) {
        val textView = getQuestionTextView(counter, q.qText)
        Quiz_Container.addView(textView)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        q.options?.forEachIndexed { index, element ->
            val checkBox = CheckBox(this)
            checkBox.id = (q.id.toString() + index.toString()).toInt()
            checkBox.text = element
            checkBox.layoutParams = params
            Quiz_Container.addView(checkBox)
        }
    }

    private fun getQuestionTextView(counter: Int, question: String): TextView {
        val textView = TextView(this)
        textView.text = "Q.${counter + 1} $question"

        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = margin.pixels }
        return textView
    }

    private fun setupSubmitButton() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.topMargin = margin.pixels
        params.gravity = Gravity.CENTER_HORIZONTAL

        val button = Button(this)
        button.layoutParams = params
        button.text = "Submit"
        button.setOnClickListener {
            evaluateQuiz()
        }
        Quiz_Container.addView(button)


    }

    private fun evaluateQuiz() {
        var score = 0

        questions.forEach { q ->
            when (q.type) {
                QuestionType.Text -> {
                    val editText = Quiz_Container.findViewById<EditText>(q.id)

                    editText?.let {
                        val userAnswer = (it.text.toString()).lowercase()
                        if (userAnswer == q.answers[0].lowercase()) {
                            score++
                        }
                    }

                }
                QuestionType.Radio -> {
                    val radioGroup = Quiz_Container.findViewById<RadioGroup>(q.id)

                    radioGroup?.let {
                        val checkId = it.checkedRadioButtonId  /*Returns the id of the radio button selected*/
                        if (checkId > 0) {
                            val radioButton = Quiz_Container.findViewById<RadioButton>(checkId)
                            val userAnswer = radioButton.text
                            if (userAnswer == q.answers[0]) {
                                score++
                            }
                        }


                    }
                }
                QuestionType.Checkbox -> {
                    var correct = true

                    q.options?.forEachIndexed { index, element ->
                        val checkedId = (q.id.toString() + index.toString()).toInt()
                        val checkBox = Quiz_Container.findViewById<CheckBox>(checkedId)
                        if (q.answers.contains(checkBox.text)) {
                            if (!checkBox.isChecked) {
                                correct = false
                            }
                        } else {
                            if (checkBox.isChecked) {
                                correct = false
                            }
                        }
                    }
                    if (correct) score++
                }
            }
        }
        Toast.makeText(this,"You scored $score out of ${questions.size}",Toast.LENGTH_SHORT).show()
    }
}