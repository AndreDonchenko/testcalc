package ua.goit.andre.testcalc;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;

import java.util.HashMap;

import static spark.Spark.*;

/**
 * Created by Andre on 15.05.2016.
 */
public class Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);
    private static Evaluator evaluator = new Evaluator();
    private static final String[] result = new String[1];

    public static void main(String[] args) {

        HistoryDB.initDatabase();

        port(8080);
        staticFileLocation("/public");
        post("/api", (req, res) -> {
            LOGGER.info("get  argument: " + req.body());
            try {
                result[0] = req.body() + " = " + evaluator.evaluate(req.body());
            } catch (EvaluationException e) {
                result[0] = "Unsupported expression!!!";
            }
            LOGGER.info("return result: " + result[0]);
            HistoryDB.addHistory(req.body(), result[0]);
            return result[0];
        });
        get("/history", (req, res) -> {
            HashMap model = new HashMap();
            model.put("template",  HistoryDB.getHistory());
            return new ModelAndView(model, "templates/history.vtl");
        }, new HTMLTemplateEngine());
  }
}
