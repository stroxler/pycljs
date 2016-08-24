(ns pycljs.core
  (:require [reagent.core :as reagent :refer [atom]]))


(defn json-parse-js [string]
  ((.-parse js/JSON) string))

(defn json-parse [string]
  (-> string (json-parse-js) (js->clj)))

(enable-console-print!)

(println "This text is printed from src/pycljs/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))



(defn add-load-func [f]
  (let [old-onload (.-onload js/window)]
    (set! (.-onload js/window)
          (if (nil? old-onload)
            f
            (fn [_] (old-onload) (f))))))


(defn make-request [method url async handler]
  (let [req         (js/XMLHttpRequest.)
        req-ok      (fn [] (= (.-status req) 200))
        data-ready  (fn [] (= (.-readyState req) 4))
        req-done    (fn [] (and (req-ok) (data-ready)))
        parse-resp  (fn [] (-> req (.-responseText) (json-parse)))
        handle-resp (fn [_]
                      (when (req-done)
                        (let [response (parse-resp)]
                          (handler response))))]
    (set! (.-onreadystatechange req) handle-resp)
    (.open req method url async)
    (.send req)))


(add-load-func
  (fn []
    (make-request
      "GET"
      "/newtext"
      true
      (fn [response]
        ;; notes:
        ;;  (assoc-in obj keyseq value) associates value with a
        ;;                              series of keys in a nested map
        ;;  (swap! atom assoc-in keyseq value)  does the same operation
        ;;                                      on a map inside an atom
        (swap! app-state assoc-in [:text] (response "text"))))))


(defn hello-world []
  [:h1 "Flask with clojurescript and figwheel demo"
   [:p "The heading below should change to say hi from flask"]
   [:h2 (:text @app-state)]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
