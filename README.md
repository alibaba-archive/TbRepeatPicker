# RecurrenceRule
RecurrenceRule

This is  a  RecurrenceRule libray that based on [Google RFC2445](http://tools.ietf.org/html/rfc2445#section-4.3.10)

![](https://github.com/nevermoresss/RecurrenceRule/blob/master/demo.gif)


#How?
```java
RecurrenceRuleHelper recurrenceRuleHelper = new RecurrenceRuleHelper(context,startDate,currentRules,
                new OnRecurrenceSetListener() { 
                    @Override
                    public void onRecurrenceSet(String recurrenceRule, String paresedStr) {
                    //do something
                    }
                },yourRules);

recurrenceRuleHelper.setRecurrence();
```



The Deafule Rule is **TbRule** (Our Company is Teambition), you can also use your own **Rule** by extends abstract class **Rule**;






