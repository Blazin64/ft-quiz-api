Spackage com.cooksys.quiz_api.controllers;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

  private final QuizService quizService;

  @GetMapping
  public List<QuizResponseDto> getAllQuizzes() { return quizService.getAllQuizzes(); }

  @PostMapping
  public QuizResponseDto addQuiz(@RequestBody QuizRequestDto quizRequestDto) { return quizService.addQuizFromRequest(quizRequestDto); }

  @DeleteMapping("/{id}")
  public QuizResponseDto deleteQuiz(@PathVariable Long id) { return quizService.deleteQuizById(id); }

  @PatchMapping("/{id}/rename/{newName}")
  public QuizResponseDto updateQuizName(@PathVariable Long id, @PathVariable String newName) { return quizService.updateQuizNameById(id, newName); }

  @GetMapping("/{id}/random")
  public QuestionResponseDto getRandomQuestion(@PathVariable Long id) { return quizService.getRandomQuestion(id); }

  @PatchMapping("/{id}/add")
  public QuizResponseDto addQuestionToQuiz(@PathVariable Long id, @RequestBody QuestionRequestDto questionRequestDto) { return quizService.addQuestionToQuizById(id, questionRequestDto); }

  @DeleteMapping("/{id}/delete/{questionID}")
  public QuestionResponseDto deleteQuestionFromQuizById(@PathVariable Long id, @PathVariable Long questionID) { return quizService.deleteQuestionFromQuizById(id, questionID); }

  //An extra endpoint that is useful for testing purposes
  @GetMapping("/{id}")
  public QuizResponseDto getQuizById(@PathVariable Long id) { return quizService.getQuizById(id); }
}
