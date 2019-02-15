(ns org.shender.clojure-gnuplot
  "Interface to gnuplot."
  (:import [java.io OutputStreamWriter BufferedWriter]))


;;; Gnuplot commands transformation.

(defmulti transform-expr
          "Transform arbitrary s-expr to string representation of corresponding infix
          expression."
          type)
(defmulti transform-funcall
          "Transform s-expr, representing function call, to string representation of
          corresponding infix expression."
          first)


(defmethod transform-expr :default [x] (str x))
(defmethod transform-expr String [x] (format "\"%s\"" x))
(defmethod transform-expr clojure.lang.PersistentList [x]
  (transform-funcall x))


(def *bin-ops* [['+ 0] ['- 0] ['* 1] ['/ 1]])

(defn transform-func
  "Transformation of function call."
  [func args]
  (let [args (map transform-expr args)]
    (format "%s(%s)" func (apply str (interpose ", " args)))))

(defn transform-bin-op
  "Transformation of binary operation."
  [op id args]
  (let [args (map transform-expr args)]
    (if (= 1 (count args))
      (transform-bin-op op id (cons id args))
      (format "(%s)" (apply str (interpose op args))))))

(defmethod transform-funcall :default [[func & args]]
  (transform-func func args))

(defmethod transform-funcall 'range [[range a b]]
  (format "[%s:%s]" a b))
(defmethod transform-funcall 'enum [[enum a b]]
  (format "%s, %s" a b))

(doseq [[op id] *bin-ops*]
  (defmethod transform-funcall op [[op & args]]
    (transform-bin-op op id args)))


(defn transform-cmd
  "Transform s-expr, representing gnuplot command, to gnuplot command."
  [cmd]
  (let [cmd (map transform-expr cmd)]
    (apply str (interpose " " cmd))))



;;; Interfacing to Gnuplot.

(defn start
  "Start gnuplot process."
  []
  (let [proc (.exec (Runtime/getRuntime) "gnuplot")
        out (-> (.getOutputStream proc) OutputStreamWriter. BufferedWriter.)]
    {:proc proc :out out}))

(defn stop
  "Stop gnuplot process."
  [gnuplot]
  (.destroy (:proc gnuplot)))

(defn send-command
  "Send command to gnuplot process."
  [gnuplot cmd]
  (let [out (:out gnuplot)]
    (.write out (str cmd \newline))
    (.flush out)))

(defmacro exec
  "Execute gnuplot commands."
  [gnuplot & cmds]
  `(doseq [cmd# '~cmds]
     (send-command ~gnuplot (transform-cmd cmd#))))
