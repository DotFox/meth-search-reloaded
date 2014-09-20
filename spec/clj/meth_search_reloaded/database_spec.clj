(ns meth-search-reloaded.database-spec
  (:require [speclj.core :refer :all]
            [datomic.api :as d]))

(defn create-empty-in-memory-database []
  (let [uri "datomic:mem://meth-search-reloaded-spec"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)
          schema (load-file "schema.edn")]
      (d/transact conn schema)
      conn)))

(describe "Tests for database module"
          )
