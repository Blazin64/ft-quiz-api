package com.cooksys.quiz_api.services;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;

public interface QuizService {

  List<QuizResponseDto> getAllQuizzes();

  Quiz getQuiz(Long id);

  QuizResponseDto getQuizById(Long id);

  Question getQuestion(Long id);

  QuestionResponseDto getRandomQuestion(Long id);

  Quiz updateQuizName(Long id, String name);

  QuizResponseDto updateQuizNameById(Long id, String name);

  void addAnswerToQuestion(Long id, Answer answer);

  Quiz addQuestionToQuiz(Long id, Question question);

  QuizResponseDto addQuestionToQuizById(Long id, QuestionRequestDto questionDto);

  Quiz addQuiz(Quiz quiz);

  QuizResponseDto addQuizFromRequest(QuizRequestDto quizRequestDto);

  Quiz deleteQuiz(Long id);

  QuizResponseDto deleteQuizById(Long id);

  Question deleteQuestionFromQuiz(Long id, Long questionId);

  QuestionResponseDto deleteQuestionFromQuizById(Long id, Long questionId);

}
