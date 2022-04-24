(ns toylang-lsp.main
  (:require [instaparse.core :as insta]))


(def toy-lang-v1
  (insta/parser
    "program = (<whitespace?> body)* <whitespace?>
     <body> = var_def | fn_def | statement
     var_def = <'var'> <whitespace> ident <whitespace?> <';'>
     fn_def = <'fn'> <whitespace> ident <whitespace? '{'> fn_body <'}'>
     fn_body = (<whitespace?> body)* <whitespace?>
     statement = ident (<whitespace> ident)* <';'>
     ident = !'var' #'[a-zA-Z_][a-zA-Z0-9_]+'
     whitespace = #'\\s+'
     number = #'[0-9]+'"))

(defn parse-line-col-meta [parser-fn s]
  (insta/add-line-and-column-info-to-metadata s (parser-fn s)))

;; LSP documentation on DocumentSymbol:
;; https://microsoft.github.io/language-server-protocol/specification#textDocument_documentSymbol

;; Search that page for "SymbolKind" to find the following definitions:

(def lsp-symbol-kind->numeric-code
  {:file 1
   :module 2
   :namespace 3
   :package 4
   :class 5
   :method 6
   :property 7
   :field 8
   :constructor 9
   :enum 10
   :interface 11
   :function 12
   :variable 13
   :constant 14
   :string 15
   :number 16
   :boolean 17
   :array 18
   :object 19
   :key 20
   :null 21
   :enum-member 22
   :struct 23
   :event 24
   :operator 25
   :type-parameter 26})

(defn insta-loc->lsp-loc [insta-loc]
  (let [{:instaparse.gll/keys [start-line start-column end-line end-column]}
        insta-loc]
    {"start" {"line" (dec start-line)
              "character" (dec start-column)}
     "end"   {"line" (dec end-line)
              "character" (dec end-column)}}))

(defn parse->doc-symbol-helper [parse-node]
  (let [node-type (first parse-node)
        ident-node (second parse-node)
        ident-str (second ident-node)]
    (case node-type
      :fn_def
      (let [children-nodes (rest (nth parse-node 2))
            children-xformed (mapcat parse->doc-symbol-helper children-nodes)
            base-map {"name" ident-str
                      "kind" (lsp-symbol-kind->numeric-code :function)
                      "range" (insta-loc->lsp-loc (meta parse-node))
                      "selectionRange" (insta-loc->lsp-loc (meta ident-node))}]
        (if (zero? (count children-xformed))
          [base-map]
          [(assoc base-map "children" (vec children-xformed))]))
      
      :var_def
      [{"name" ident-str
        "kind" (lsp-symbol-kind->numeric-code :variable)
        "range" (insta-loc->lsp-loc (meta parse-node))
        "selectionRange" (insta-loc->lsp-loc (meta ident-node))}]
      
      ;; all other cases
      nil)))

(defn parse-rslt->document-symbol-info [parse-rslt jsonrpc-id fname]
  (let [children-nodes (rest parse-rslt)
        children-xformed (mapcat parse->doc-symbol-helper children-nodes)]
    {"jsonrpc" "2.0",
     "id" jsonrpc-id
     "result" [{"name" fname
                "kind" (lsp-symbol-kind->numeric-code :file)
                "range" {"start" {"line" 0, "character" 0}
                         "end" {"line" 999999, "character" 999999}}
                "selectionRange" {"start" {"line" 0, "character" 0}
                                  "end" {"line" 999999, "character" 999999}}
                "children" (vec children-xformed)}]}))


(comment

(do
(require '[instaparse.core :as insta])
(use 'clojure.pprint)
)

(def toy-prog1 (slurp "toyprogs/prog1.toy"))
(def p1 (toy-lang-v1 toy-prog1))
(def p1 (parse-line-col-meta toy-lang-v1 toy-prog1))
(pprint p1)
(binding [*print-meta* true] (pprint p1))
(def r1 (parse-rslt->document-symbol-info p1 78 "prog1.toy"))
(pprint r1)

(def toy-prog2 (slurp "toyprogs/prog2.toy"))
(def p2 (toy-lang-v1 toy-prog2))
(def p2 (parse-line-col-meta toy-lang-v1 toy-prog2))
(pprint p2)
(binding [*print-meta* true] (pprint p2))
(def r2 (parse-rslt->document-symbol-info p2 78 "prog2.toy"))
(pprint r2)

(def toy-prog3 (slurp "toyprogs/prog3.toy"))
(def p3 (toy-lang-v1 toy-prog3))
(def p3 (parse-line-col-meta toy-lang-v1 toy-prog3))
(pprint p3)
(binding [*print-meta* true] (pprint p3))
(def r3 (parse-rslt->document-symbol-info p3 78 "prog3.toy"))
(pprint r3)

)
