(ns clojurekalas.core
  (:gen-class))

(import javax.swing.JFrame)
(import javax.swing.JButton)

(def persons '({:id 1 :name "olle"} {:id 2 :name "anna"} {:id 3 :name
"isak"} {:id 4 :name "beatrice"}))

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

(defmacro select
	[args table]
	`(get ~table 0))

(defmacro from
	[table]
	`(get ~table 0))


(defmacro destrTest
	[{:keys [id name]}]
	`(str ~id " " ~name))
	;`(let [{name :name id :id} peter]
	;	(println ~'id " " ~'name)))

(defmacro destrTest2
	[map]
	`(let [{name :name id :id} map]
		(str ~'id " " ~'name)))
