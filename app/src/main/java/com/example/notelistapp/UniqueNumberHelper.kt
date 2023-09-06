package com.example.notelistapp

 class UniqueNumberHelper {
  public  var lastNumber:Int = 0


     public fun produceUid(): Int {

         var now=System.currentTimeMillis().toString();
        var num= now.subSequence(4,now.length-1)

         this.lastNumber=num.toString().toInt()

         return num.toString().toInt()
     }


 }