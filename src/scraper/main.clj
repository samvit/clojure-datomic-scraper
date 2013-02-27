(ns scraper.main
  (:require [net.cgrand.enlive-html :as html]))

(def *base-url* "http://news.ycombinator.com/")

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn hn-headlines-and-points []
  (map html/text
       (html/select (fetch-url *base-url*)
                    #{ 
                      [:td.title (:not(:a [:rel "nofollow"]))] 
                      [:td.subtext (html/nth-child 3)]
                      ;;[:td.subtext :a html/first-child]
                      })))

(defn selector [hiccup]
  (map html/text
       (html/select (fetch-url *base-url*)
                    #{ 
                      hiccup
                      }
                    ))
  )

(defn hn-comments []
  (selector [:td.title (:not(:a [:rel "nofollow"]))]))

(defn hn-comments []
  (map html/text
       (html/select (fetch-url *base-url*)
                    #{ 
                      [:td.subtext (html/nth-child 3)]
                      }
                    )))
(defn hn-points []
  (map html/text
       (html/select (fetch-url *base-url*)
                    #{ 
                      [:td.subtext (html/first-child 3)]
                      }
                    )))

(defn hn-headlines []
  (map html/text
       (html/select (fetch-url *base-url*)
                    #{ 
                      [:td.title (:not(:a [:rel "nofollow"]))] 
                      }
                    )))               

(defn print-headlines-and-points [sel-func]
  (doseq [line (map (fn [[title]] 
                      (str title)
                      )
                    (partition 1 (sel-func)))]
    (println line)))

(defn run-all []
  (print-headlines-and-points hn-headlines)
  (print-headlines-and-points hn-comments)
  (print-headlines-and-points hn-points)
  )
