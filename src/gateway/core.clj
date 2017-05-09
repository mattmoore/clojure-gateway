(ns gateway.core
  (:gen-class))

(use 'ring.adapter.jetty)
(require '[http.async.client :as http])

(defn parse-int [number-string]
  (if (= 0 (count number-string))
    (throw (Exception. "No number provided.")))
  (try (Integer/parseInt number-string)
       (catch Exception e (throw e))))

(defn port [args]
  (as-> args v
    (first v)
    (try (parse-int v)
         (catch Exception e
           (throw (Exception. (str "Invalid port number. " (.getMessage e))))))))

(defn -main
  [& args]
  (with-open [client (http/create-client)]
  (let [response (http/GET client (format "http://localhost:%d/people", (port args)))]
    (-> response
        http/await
        http/string
        println))))
