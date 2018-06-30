(ns hport.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [hport.core-test]
   [hport.common-test]))

(enable-console-print!)

(doo-tests 'hport.core-test
           'hport.common-test)
