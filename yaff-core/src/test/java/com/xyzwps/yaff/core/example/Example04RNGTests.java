package com.xyzwps.yaff.core.example;

import com.xyzwps.yaff.core.TestCommons;
import com.xyzwps.yaff.core.FlowContext;
import org.junit.jupiter.api.Test;

import static com.xyzwps.yaff.core.TestCommons.factory;
import static org.junit.jupiter.api.Assertions.*;

class Example04RNGTests {

    @Test
    void testRngNode() {

        var flowJSON = """
                {
                  "flowNodes": [
                    {
                      "id": "start",
                      "name": "control.start",
                      "px": 160,
                      "py": 300,
                      "edges": [{ "to": "n1750350189893", "type": "FALLBACK", "expression": null }]
                    },
                    {
                      "id": "n1750350189893",
                      "name": "yaff.rng",
                      "ref": "r",
                      "description": "",
                      "px": 432.185899433957,
                      "py": 256.78064183120574,
                      "edges": [
                        {
                          "to": "n1750350273531",
                          "type": "CHECK",
                          "expression": { "type": "javascript", "expression": "r < 34" }
                        },
                        {
                          "to": "n1750350293432",
                          "type": "CHECK",
                          "expression": { "type": "javascript", "expression": "r < 67 && r >= 34" }
                        },
                        { "to": "n1750350311521", "type": "FALLBACK", "expression": null }
                      ],
                      "assignExpressions": [
                        { "expression": "0", "inputName": "min", "type": "javascript" },
                        { "expression": "100", "inputName": "max", "type": "javascript" }
                      ]
                    },
                    {
                      "id": "n1750350273531",
                      "name": "demo.sendMessage",
                      "description": "",
                      "px": 1581.7114242356513,
                      "py": 129.17661639821392,
                      "assignExpressions": [
                        { "expression": "\\"小随机数\\"", "inputName": "title", "type": "javascript" },
                        { "expression": "\\"小随机数已生成\\"", "inputName": "message", "type": "javascript" }
                      ]
                    },
                    {
                      "id": "n1750350293432",
                      "name": "demo.sendMessage",
                      "description": "",
                      "px": 1582.6105736272436,
                      "py": 366.552055778639,
                      "assignExpressions": [
                        { "expression": "\\"中随机数\\"", "inputName": "title", "type": "javascript" },
                        { "expression": "\\"中随机数已生成\\"", "inputName": "message", "type": "javascript" }
                      ]
                    },
                    {
                      "id": "n1750350311521",
                      "name": "demo.sendMessage",
                      "description": "",
                      "px": 1583.5097230188364,
                      "py": 613.8181384665817,
                      "assignExpressions": [
                        { "expression": "\\"大随机数\\"", "inputName": "title", "type": "javascript" },
                        { "expression": "\\"大随机数已生成\\"", "inputName": "message", "type": "javascript" }
                      ]
                    }
                  ]
                }
                """;
        var flow = factory.fromJSON(flowJSON);
        var executor = factory.getExecutor();

        int low = 0;
        int mid = 0;
        int high = 0;

        for (int i = 0; i < 100; i++) {
            var ctx = FlowContext.create();
            assertNull(ctx.get("r"));

            executor.execute(flow, ctx);

            var r = ctx.get("r");
            assertNotNull(r);
            assertInstanceOf(Double.class, r);

            var v = (Double) r;
            if (v < 34) {
                low++;
                assertEquals("[小随机数]: 小随机数已生成", TestCommons.MESSAGES_RECEIVER.getLast());
            } else if (v < 67) {
                mid++;
                assertEquals("[中随机数]: 中随机数已生成", TestCommons.MESSAGES_RECEIVER.getLast());
            } else {
                high++;
                assertEquals("[大随机数]: 大随机数已生成", TestCommons.MESSAGES_RECEIVER.getLast());
            }
        }

        // 检查一下结果分布，料敌从宽
        assertTrue(low > 20 && low < 50);
        assertTrue(mid > 20 && mid < 50);
        assertTrue(high > 20 && high < 50);
    }
}
