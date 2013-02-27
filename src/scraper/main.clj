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

(defn print-headlines-and-points []
  (doseq [line (map (fn [[title comments]] 
                      (str title "\n" "\n")
                      ;; { :title title
                      ;;                :points points
                      ;;                :author author 
                      ;;                :comments (.replace "comments" "" comments)
                      ;;                }
                      )
                    (partition 2 (hn-headlines-and-points)))]
    (println line)))
