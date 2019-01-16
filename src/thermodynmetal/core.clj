(ns thermodynmetal.core
  (:gen-class))
(use 'thermodynmetal.miedema)
(use 'seesaw.core)
(use 'seesaw.mig)
(import '(org.knowm.xchart XChartPanel)
        '(java.io File))

(def atoms {"Ag" Ag "Cu" Cu "Zn" Zn "Fe" Fe "C" C "Al" Al "Mg" Mg "Ni" Ni "N" N "H" H "Y" Y "Cr" Cr "Sr" Sr "Sc" Sc "Ti" Ti "V" V "Mo" Mo "Co" Co "Li" Li "Na" Na "Ga" Ga "In" In "Tl" Tl "Sn" Sn "Pb" Pb "Sb" Sb "Bi" Bi "Pd" Pd "Au" Au "Mn" Mn "Zr" Zr "Nb" Nb "Tc" Tc "Ta" Ta "W" W "Pt" Pt "La" La "Re" Re "Rh" Rh "Ru" Ru "Gd" Gd "Ca" Ca "B" B "Cd" Cd})
;;(plot Zn Cu)
(native!)
(def myicon (icon (File. "ico.ico")))
(def f (frame :title "Miedema for binary"
              :icon myicon
              :width 456
              :height 456))
;;(-> f pack! show!)
;;(-> f show!)
(defn display [content]
  (config! f :content content)
  content)
(def myframe (frame :title "Miedema Enthalpy"
                    :icon myicon
                    :width 888
                    :height 666))
;;(def mypanel (grid-panel :rows 3 :columns 3 ))
(def soluteLabel (label "solute:"))
(def solventLabel (label "solvent:"))
(def soluteText (text "Zn"))
(def solventText (text "Cu"))
(def myButton (button :text "OK"))
(def logs (text :multi-line? true :rows 30 :text "Results are shown here"))
(def myPanel (mig-panel
               :constraints ["" "[shrink 0]20px[200, grow, fill]" "[shrink 0]5px[grow]"]
               :items [[soluteLabel]
                       [soluteText]
                       [solventLabel]
                       [solventText]
                       [myButton "wrap"]
                       [(scrollable logs) "cell 1 2 4 30"]]))
(display myPanel)
;;(config! myframe :content (XChartPanel. (getChart Zn Cu)))
(listen myButton :action (fn [e]
                           (let [solute (atoms (text soluteText)) solvent (atoms (text solventText))]
                             (config! myframe :content (XChartPanel. (getChart solute solvent)))
                             (text! logs (str (text logs) "\nsolvent-solute(" (:name solvent) "-" (:name solute) ")\n"
                                              "Heat of solution(kJ/mol):\n"
                                              (str (:name solute) " in " (:name solvent) ":" (format "%.2f" (heatOfSolution solute solvent)) "\n")
                                              (str (:name solvent) " in " (:name solute) ":" (format "%.2f" (heatOfSolution solvent solute)) "\n")
                                              "Grain boundary segregation enthalpy(kJ/mol):\n"
                                              (str (format "%.2f" (GBEnthalpy solute solvent)))))

                             ;;(config! f :visible? false)
                             ;;(config! f :visible? true)
                             (config! myframe :visible? true))))

;;(config! myframe :visible? true)
;;(text logs)
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (-> f show!))
;;(println "Hello, World!")

