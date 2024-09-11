package com.cooksys.quiz_api.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.exceptions.NotFoundException;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.repositories.AnswerRepository;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

  private final QuestionMapper questionMapper;
  private final QuizRepository quizRepository;
  private final QuestionRepository questionRepository;
  private final AnswerRepository answerRepository;
  private final QuizMapper quizMapper;

  @Override
  public List<QuizResponseDto> getAllQuizzes() {
    return quizMapper.entitiesToDtos(quizRepository.findAll());
  }

  @Override
  public Quiz getQuiz(Long id) {
    Optional<Quiz> searchedQuiz = quizRepository.findById(id);

    // Make sure the quiz exists.
    if (searchedQuiz.isEmpty()) {
      throw new NotFoundException("Quiz not found! ID:" + id);
    }

    return searchedQuiz.get();
  }

  @Override
  public QuizResponseDto getQuizById(Long id) {
    return quizMapper.entityToDto(getQuiz(id));
  }

  @Override
  public Question getQuestion(Long id) {
    Optional<Question> searchedQuestion=questionRepository.findById(id);

    // Make sure the question exists.
    if (searchedQuestion.isEmpty()) {
      throw new NotFoundException("Question not found! ID:" + id);
    }

    return searchedQuestion.get();
  }

  @Override
  public QuestionResponseDto getRandomQuestion(Long id) {
    // Set up random number generation.
    Random generator = new Random();
    Quiz baseQuiz = getQuiz(id);

    // Get the questions list and return a random entry.
    List<Question> questions = baseQuiz.getQuestions();
    int index = generator.nextInt(questions.size());

    return questionMapper.entityToDto(questions.get(index));
  }

  @Override
  public Quiz updateQuizName(Long id, String name) {
    Quiz quiz = getQuiz(id);

    quiz.setName(name);

    return quizRepository.saveAndFlush(quiz);
  }

  @Override
  public QuizResponseDto updateQuizNameById(Long id, String name) {
    return quizMapper.entityToDto(updateQuizName(id, name));
  }

  @Override
  public void addAnswerToQuestion(Long id, Answer answer) {
    answer.setQuestion(getQuestion(id));

    answerRepository.saveAndFlush(answer);
  }

  @Override
  public Quiz addQuestionToQuiz(Long id, Question question) {

    question.setQuiz(getQuiz(id));

    questionRepository.saveAndFlush(question);

    // Add all the answers in the question.
    for (Answer answer: question.getAnswers()) {
      addAnswerToQuestion(question.getId(), answer);
    }

    return getQuiz(id);
  }

  @Override
  public QuizResponseDto addQuestionToQuizById(Long id, QuestionRequestDto questionDto) {
    Question question = questionMapper.requestDtoToEntity(questionDto);
    return quizMapper.entityToDto(addQuestionToQuiz(id, question));
  }

  @Override
  public Quiz addQuiz(Quiz quiz) {
    quizRepository.saveAndFlush(quiz);

    Long id = quiz.getId();

    // Add all questions in the quiz.
    for (Question question: quiz.getQuestions()) {
      addQuestionToQuiz(id, question);
    }

    return getQuiz(id);
  }

  @Override
  public QuizResponseDto addQuizFromRequest(QuizRequestDto quizRequestDto) {
    Quiz quiz = quizMapper.requestDtoToEntity(quizRequestDto);
    return quizMapper.entityToDto(addQuiz(quiz));
  }

  @Override
  public Quiz deleteQuiz(Long id) {
    // Temporary storage for the deleted quiz.
    // This allows it to be returned.
    Quiz quiz = getQuiz(id);

    // Delete all the questions first.
    for (Question question: quiz.getQuestions()) {
      deleteQuestionFromQuiz(quiz.getId(), question.getId());
    }

    // Delete the quiz.
    quizRepository.delete(quiz);

    return quiz;
  }

  @Override
  public QuizResponseDto deleteQuizById(Long id) {
    return quizMapper.entityToDto(deleteQuiz(id));
  }

  @Override
  public Question deleteQuestionFromQuiz(Long id, Long questionID) {
    Quiz quiz = getQuiz(id);

    // Temporary storage for the deleted question.
    // This allows it to be returned.
    Question question = getQuestion(questionID);

    // Make sure the quiz actually contains this question
    if ( !quiz.getQuestions().contains(question)) {
      throw new NotFoundException("The quiz does not contain that question. ID: " + id + " questionID: " + questionID);
    }

    // Delete all the answers first.
    List<Answer> answers = question.getAnswers();
    for (Answer answer : answers) {
      answerRepository.delete(answer);
    }

    // Delete the question.
    questionRepository.delete(question);

    return question; }

  @Override
  public QuestionResponseDto deleteQuestionFromQuizById(Long id, Long questionID) {
    return questionMapper.entityToDto(deleteQuestionFromQuiz(id, questionID));
  }

}
