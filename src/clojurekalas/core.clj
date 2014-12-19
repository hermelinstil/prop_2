(ns clojurekalas.core
  (:gen-class))

(import javax.swing.JFrame)
(import javax.swing.JButton)



(def olle '{:id 1 :name "olle"})
(def peter {:id 2 :name "peter"})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn initGUI
	[]
	(def frame (JFrame.))
	(.add frame(JButton. "KNAPP"))
	(.setSize frame 200 200)
	(.setVisible frame true))

(defn echo
	[& x]
	(println x))

(defmacro echo-macro
	[& x]
	(println x))

(defn sqr-func
	[x]
	(* x x))

(defmacro sqr-macro
	[x]
	(* x x))

(defmacro make-adder-macro
	[x]
	(fn [y] (+ x y)))

(defmacro print-java-string
	[object]
	`(if 
		(instance? java.lang.String ~object)
		(doto (new java.lang.String "DET GICK BRA") (.toString))
		"FALSE"))

(defmacro safe
	[variable value expression]
	`((def ~variable ~value)
		(~expression ~variable)))

(defmacro safe2
	[variable value expression]
	`(let [~variable ~value] ~expression))

(defmacro safe3
	[variable expression]
	`(let [~(get variable 0) ~(get variable 1)] 
		~expression))

(defmacro safe4
	[variable expression]
	`(if (instance? java.io.Closeable ~(get variable 1))
		(try
			(let [~(get variable 0) ~(get variable 1)] 
				~expression)

			(catch IOException f#
				(str "caught exception: " (.getMessage f#)))
			(finally (println "FINALLY")))
		(println "NOT CLOSEABLE")))


(import java.io.FileReader)
(import java.io.File)
(import java.io.IOException)
;new File() kollas inte i try, ska den göra det?
;ska den kolla fler parametrar?
(defmacro safe5
	"variable => [name instance] \n  Evaluates the expression on the instance"

	[variable expression]
	`(if (instance? java.io.Closeable ~(get variable 1))
		
			(let [~(get variable 0) ~(get variable 1)] 
				(try 
					~expression
				;(catch IOException ~'f
				;	(str "caught exception: " (.getMessage ~'f)))
				;(catch java.io.FileNotFoundException f#
				;	(str "caught exception: " (.getMessage f#)))
				(finally (.close ~(get variable 0)))))

			
		(throw (IllegalArgumentException. "Input was not closeable"))))

(def persons '({:age 12 :id 2 :name "olle"} {:age 3 :id 1 :name "anna"} {:age 54 :id 3 :name
"isak"} {:age 10796 :id 4 :name "beatrice"}))

;(defmacro select
;	[columns table]
;	`(loop [resultTable ~[] restTable ~table]
;		(when (zero? (count ~restTable))
;		(if (<  ~(get (first table) :id) 2)
;			(recur (conj ~resultTable ~(get (first table)) (get (rest ~table))))
;			(recur ~resultTable (get (rest ~table)))))))

(defmacro query
	[selectColumns whereCondition fromTable orderByCondition]
	`(vals (orderBy ~orderByCondition (where ~whereCondition (select ~selectColumns ~fromTable)))))

(defn select
	[columnNames table]
	(loop [resultTable [] restTable table]
		(if (zero? (count restTable))
			resultTable
			(recur (conj resultTable  (select-keys (first restTable) columnNames)) (rest restTable)))))

(defn where
	[[operator columnName value] table]
	(loop [resultTable [] restTable table]
		(if (zero? (count restTable))
			resultTable
		(if (operator (get (first restTable) columnName) value)
			(recur (conj resultTable (first restTable)) (rest restTable))
			(recur resultTable (rest restTable))))))


(defn orderBy
	[columnName table]
	(loop [resultTable (sorted-map) restTable table]
		(if (zero? (count restTable))
			resultTable
			(recur (into resultTable {(get (first restTable) columnName) (first restTable)}) (rest restTable)))))


(defmacro multibrackets
	[[sym init] body]
	`(let [~sym ~init] ~body))

(defmacro destrTest
	[{:keys [id name]}]
	`(str ~id " " ~name))
	;`(let [{name :name id :id} peter]
	;	(println ~'id " " ~'name)))

(defmacro destrTest2
	[map]
	`(let [{name :name id :id} map]
		(str ~'id " " ~'name)))
