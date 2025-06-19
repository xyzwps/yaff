package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.FlowContext;
import org.junit.jupiter.api.Test;

import static com.xyzwps.libs.yaff.example.Commons.factory;
import static org.junit.jupiter.api.Assertions.*;

class Example04Tests {

    @Test
    void test() {

        var flowJSON = """
                {
                  "flowNodes": [
                    {
                      "id": "start",
                      "name": "control.start",
                      "px": 160,
                      "py": 300,
                      "next": ["n1750350189893"]
                    },
                    {
                      "id": "n1750350189893",
                      "name": "demo.rng",
                      "ref": "r",
                      "description": "",
                      "px": 432.185899433957,
                      "py": 256.78064183120574,
                      "next": ["n1750350208846"],
                      "assignExpressions": [
                        { "expression": "0", "inputName": "min", "type": "javascript" },
                        { "expression": "100", "inputName": "max", "type": "javascript" }
                      ]
                    },
                    {
                      "id": "n1750350208846",
                      "name": "control.case",
                      "description": "",
                      "px": 890.9318874101455,
                      "py": 360.5653805558567,
                      "next": ["n1750350213622", "n1750350231754", "n1750350260274"],
                      "assignExpressions": []
                    },
                    {
                      "id": "n1750350213622",
                      "name": "control.when",
                      "description": "",
                      "px": 1145.5969605832388,
                      "py": 193.11437353793238,
                      "next": ["n1750350273531"],
                      "assignExpressions": [
                        {
                          "expression": "r < 34",
                          "inputName": "condition",
                          "type": "javascript"
                        }
                      ]
                    },
                    {
                      "id": "n1750350231754",
                      "name": "control.when",
                      "description": "",
                      "px": 1150.8298045525487,
                      "py": 413.7659609105097,
                      "next": ["n1750350293432"],
                      "assignExpressions": [
                        {
                          "expression": "r < 67 && r >= 34",
                          "inputName": "condition",
                          "type": "javascript"
                        }
                      ]
                    },
                    {
                      "id": "n1750350260274",
                      "name": "control.default",
                      "description": "",
                      "px": 1150.1197162712422,
                      "py": 638.0951720395797,
                      "next": ["n1750350311521"],
                      "assignExpressions": []
                    },
                    {
                      "id": "n1750350273531",
                      "name": "demo.sendMessage",
                      "description": "",
                      "px": 1581.7114242356513,
                      "py": 129.17661639821392,
                      "next": [],
                      "assignExpressions": [
                        {
                          "expression": "\\"小随机数\\"",
                          "inputName": "title",
                          "type": "javascript"
                        },
                        {
                          "expression": "\\"小随机数已生成\\"",
                          "inputName": "message",
                          "type": "javascript"
                        }
                      ]
                    },
                    {
                      "id": "n1750350293432",
                      "name": "demo.sendMessage",
                      "description": "",
                      "px": 1582.6105736272436,
                      "py": 366.552055778639,
                      "next": [],
                      "assignExpressions": [
                        {
                          "expression": "\\"中随机数\\"",
                          "inputName": "title",
                          "type": "javascript"
                        },
                        {
                          "expression": "\\"中随机数已生成\\"",
                          "inputName": "message",
                          "type": "javascript"
                        }
                      ]
                    },
                    {
                      "id": "n1750350311521",
                      "name": "demo.sendMessage",
                      "description": "",
                      "px": 1583.5097230188364,
                      "py": 613.8181384665817,
                      "next": [],
                      "assignExpressions": [
                        {
                          "expression": "\\"大随机数\\"",
                          "inputName": "title",
                          "type": "javascript"
                        },
                        {
                          "expression": "\\"大随机数已生成\\"",
                          "inputName": "message",
                          "type": "javascript"
                        }
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
                assertEquals("[小随机数]: 小随机数已生成", Commons.MESSAGES_RECEIVER.getLast());
            } else if (v < 67) {
                mid++;
                assertEquals("[中随机数]: 中随机数已生成", Commons.MESSAGES_RECEIVER.getLast());
            } else {
                high++;
                assertEquals("[大随机数]: 大随机数已生成", Commons.MESSAGES_RECEIVER.getLast());
            }
        }

        // 检查一下结果分布，料敌从宽
        assertTrue(low > 20 && low < 50);
        assertTrue(mid > 20 && mid < 50);
        assertTrue(high > 20 && high < 50);
    }
}
