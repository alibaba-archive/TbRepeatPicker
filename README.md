# RepeatPicker
RepeatPicker

This is  a  RepeatPicker libray that based on [Google RFC2445](http://tools.ietf.org/html/rfc2445#section-4.3.10)

![](https://github.com/teambition/TbRepeatPicker/blob/master/demo.gif)


#How?
```java
RecurrenceRuleHelper recurrenceRuleHelper = new RecurrenceRuleHelper(context
                new OnRecurrenceSetListener() { 
                    @Override
                    public void onRecurrenceSet(String recurrenceRule, String paresedStr) {
                    //do something
                    }
                });
recurrenceRuleHelper.setrRules(recurrenceRule);
recurrenceRuleHelper.setStartDate(startDate);
recurrenceRuleHelper.startSetRecurrence();
```



The Deafule Rule is **TbRule** , you can also use your own **Rule** by extends abstract class **Rule**;






