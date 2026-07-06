package io.github.delokoseni.rag_search.controller;

import io.github.delokoseni.rag_search.evaluation.EvaluationQuestion;
import io.github.delokoseni.rag_search.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping("/run")
    public String run() {

        List<EvaluationQuestion> questions = List.of(

                new EvaluationQuestion(
                        "q1",
                        "Что такое biased locking в JVM?",
                        "Biased locking — это оптимизация JVM, уменьшающая стоимость синхронизации для потоков, которые многократно захватывают один и тот же монитор."
                ),

                new EvaluationQuestion(
                        "q2",
                        "Как работает ключевое слово volatile в Java?",
                        "volatile гарантирует видимость изменений переменной между потоками и запрещает некоторые виды переупорядочивания инструкций."
                ),

                new EvaluationQuestion(
                        "q3",
                        "Чем отличается volatile от synchronized?",
                        "volatile обеспечивает видимость, но не атомарность, тогда как synchronized обеспечивает и атомарность, и взаимное исключение."

                ),

                new EvaluationQuestion(
                        "q4",
                        "Что такое Java Memory Model?",
                        "Java Memory Model определяет правила взаимодействия потоков с памятью, включая порядок операций и видимость изменений."
                ),

                new EvaluationQuestion(
                        "q5",
                        "Что означает happens-before в Java?",
                        "happens-before — это отношение, определяющее порядок, в котором операции в разных потоках становятся видимыми друг для друга."
                ),

                new EvaluationQuestion(
                        "q6",
                        "Почему двойная проверка блокировки (double-checked locking) может быть опасной?",
                        "Без volatile двойная проверка блокировки может привести к публикации частично сконструированного объекта."

                ),

                new EvaluationQuestion(
                        "q7",
                        "Какие бывают типы блокировок в Java?",
                        "В Java есть intrinsic locks (synchronized), ReentrantLock и другие реализации из java.util.concurrent.locks."
                ),

                new EvaluationQuestion(
                        "q8",
                        "Чем ReentrantLock отличается от synchronized?",
                        "ReentrantLock предоставляет больше возможностей, включая попытку захвата блокировки, таймауты и прерывание ожидания."
                ),

                new EvaluationQuestion(
                        "q9",
                        "Что такое fairness в ReentrantLock?",
                        "Fairness означает порядок предоставления блокировки потокам в порядке очереди."
                ),

                new EvaluationQuestion(
                        "q10",
                        "Почему i++ не является атомарной операцией?",
                        "i++ состоит из чтения, увеличения и записи, что делает её неатомарной в многопоточном окружении."
                ),

                new EvaluationQuestion(
                        "q11",
                        "Для чего используются AtomicInteger?",
                        "AtomicInteger обеспечивает атомарные операции над целым числом без использования блокировок."
                ),

                new EvaluationQuestion(
                        "q12",
                        "Чем ConcurrentHashMap отличается от HashMap?",
                        "ConcurrentHashMap потокобезопасен и позволяет конкурентный доступ без полной блокировки структуры."
                ),

                new EvaluationQuestion(
                        "q13",
                        "Как работает CopyOnWriteArrayList?",
                        "CopyOnWriteArrayList создаёт копию массива при каждом изменении, обеспечивая потокобезопасность чтения."
                ),

                new EvaluationQuestion(
                        "q14",
                        "Что такое ThreadPoolExecutor?",
                        "ThreadPoolExecutor управляет пулом потоков и задачами, позволяя переиспользовать потоки."
                ),

                new EvaluationQuestion(
                        "q15",
                        "Чем fixed thread pool отличается от cached thread pool?",
                        "Fixed thread pool имеет фиксированное количество потоков, cached создаёт потоки по мере необходимости."
                ),

                new EvaluationQuestion(
                        "q16",
                        "Что может привести к livelock в многопоточности?",
                        "Livelock возникает, когда потоки активно реагируют друг на друга, но не продвигаются к выполнению задачи."
                ),

                new EvaluationQuestion(
                        "q17",
                        "Чем deadlock отличается от starvation?",
                        "Deadlock — это взаимная блокировка потоков, starvation — когда поток долго не получает ресурсы."
                ),

                new EvaluationQuestion(
                        "q18",
                        "Почему небезопасно публиковать this из конструктора?",
                        "Потому что объект может быть опубликован до завершения конструктора и стать видимым в неконсистентном состоянии."
                )

        );

        evaluationService.runEvaluation(questions);

        return "OK";
    }
}