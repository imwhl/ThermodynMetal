(ns thermodynmetal.core
  (:gen-class))
(use 'thermodynmetal.miedema)
(use 'seesaw.core)
(use 'seesaw.mig)
(use 'seesaw.table)
(import '(org.knowm.xchart XChartPanel)
        '(java.io File FileOutputStream BufferedOutputStream PrintWriter))
(def atomSets [Ag Cu Zn Fe C Al Mg Ni N H Y Cr Sr Sc Ti V Mo Co Li Na Ga In Tl Sn Pb Sb Bi Pd Au Mn Zr Nb Tc Ta W Pt La Re Rh Ru Gd Ca B Cd])
(def atoms {"Ag" Ag "Cu" Cu "Zn" Zn "Fe" Fe "C" C "Al" Al "Mg" Mg "Ni" Ni "N" N "H" H "Y" Y "Cr" Cr "Sr" Sr "Sc" Sc "Ti" Ti "V" V "Mo" Mo "Co" Co "Li" Li "Na" Na "Ga" Ga "In" In "Tl" Tl "Sn" Sn "Pb" Pb "Sb" Sb "Bi" Bi "Pd" Pd "Au" Au "Mn" Mn "Zr" Zr "Nb" Nb "Tc" Tc "Ta" Ta "W" W "Pt" Pt "La" La "Re" Re "Rh" Rh "Ru" Ru "Gd" Gd "Ca" Ca "B" B "Cd" Cd})
;;(plot Zn Cu)
(native!)
(def myicon (icon (File. "ico.ico")))
(def f (frame :title "Miedema for binary"
              :icon myicon
              :width 888
              :height 666))
;;(-> f pack! show!)
;;(-> f show!)
(defn display [content]
  (config! f :content content)
  content)
;;(config! f :width 888)
(def myframe (frame :title "Miedema Enthalpy"
                    :icon myicon
                    :width 888
                    :height 666))
;;(def mypanel (grid-panel :rows 3 :columns 3 ))
(def soluteLabel (label "solute:"))
(def solventLabel (label "solvent:"))
(def soluteText (text "Zn"))
(def solventText (text "Cu"))
(def contentLabel (label "content:"))
(def contentText (text "0.5"))
(def enthalpyValueLabel (label "Enthalpy value:"))
(def enthalpyValue (label ""))
(def enthalpyUnit (label " kJ/mol"))
(def grainSizeLabel (label "grain size:"))
(def grainSizeValueText (text "1000"))
(def sizeUnit (label "nm"))
(def myButton (button :text "Formation Enthalpy"))
(def GBEnergyBtn (button :text "GB Energy plot"))
(def GBEvsSizeBtn (button :text "GB Energy vs grain size"))
(def GBCompositionBtn (button :text "GB composition"))
(def GBCompositionVsSizeBtn (button :text "GB composition vs grain size"))
(def GBSTableB (button :text "GB Hseg"))
(def GBExcessBtn (button :text "Delta atomicR vs GB excess"))
(def GBSTable (table :model [:columns [:elements :Ag :Cu :Zn :Fe :C :Al :Mg :Ni :N :H :Y :Cr :Sr :Sc :Ti :V :Mo :Co :Li :Na :Ga :In :Tl :Sn :Pb :Sb :Bi :Pd :Au :Mn :Zr :Nb :Tc :Ta :W :Pt :La :Re :Rh :Ru :Gd :Ca :B :Cd]
                             :rows [{:elements "Ag"} {:elements "Cu"} {:elements "Zn"} {:elements "Fe"} {:elements "C"} {:elements "Al"} {:elements "Mg"} {:elements "Ni"} {:elements "N"} {:elements "H"} {:elements "Y"} {:elements "Cr"} {:elements "Sr"} {:elements "Sc"} {:elements "Ti"} {:elements "V"} {:elements "Mo"} {:elements "Co"} {:elements "Li"} {:elements "Na"} {:elements "Ga"} {:elements "In"} {:elements "Tl"} {:elements "Sn"} {:elements "Pb"} {:elements "Sb"} {:elements "Bi"} {:elements "Pd"} {:elements "Au"} {:elements "Mn"} {:elements "Zr"} {:elements "Nb"} {:elements "Tc"} {:elements "Ta"} {:elements "W"} {:elements "Pt"} {:elements "La"} {:elements "Re"} {:elements "Rh"} {:elements "Ru"} {:elements "Gd"} {:elements "Ca"} {:elements "B"} {:elements "Cd"}]]))
(def logs (text :multi-line? true :rows 30 :text "Results are shown here"))
(def myPanel (mig-panel
              :constraints ["" "[shrink 0]20px[200, grow, fill]" "[shrink 0]5px[grow]"]
              :items [[""] [soluteLabel "cell 1 0"]
                      [solventLabel "cell 2 0"]
                      [contentLabel "cell 3 0"]
                      [grainSizeLabel "cell 4 0"]
                      [enthalpyValueLabel "cell 6 0"] ["" "wrap"]
                      [""] [soluteText]
                      [solventText]
                      [contentText]
                      [grainSizeValueText]
                      [sizeUnit]
                      [enthalpyValue]
                      [enthalpyUnit] [" " "wrap"]
                      [""] [myButton]
                      [GBEnergyBtn]
                      [GBEvsSizeBtn "wrap"]
                      [""] [GBCompositionBtn]
                      [GBCompositionVsSizeBtn]
                      [GBSTableB]
                      [GBExcessBtn]
                      [(scrollable logs) "cell 1 5 7 30"]]))
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
                                              (str (:name solute) " in " (:name solvent) (format ": %.2f" (GBEnthalpy solute solvent))))) ;;(config! f :visible? false)
                             (config! enthalpyValue :text (format "%.2f" (formationEnthalpy (read-string (second (re-matches (re-pattern "(\\d+\\.{0,1}\\d+).*") (text contentText)))) solute solvent)))
                             ;;(config! f :visible? true)
                             (config! myframe :visible? true))))

(listen GBEnergyBtn :action (fn [e]
                              (let [solute (atoms (text soluteText)) solvent (atoms (text solventText))]
                                (config! myframe :content (XChartPanel. (getGBEChart solute solvent (read-string (second (re-matches (re-pattern "(\\d+\\.{0,1}\\d+).*") (text grainSizeValueText)))))))
                                (config! myframe :visible? true))))

(listen GBEvsSizeBtn :action (fn [e]
                               (let [solute (atoms (text soluteText))
                                     solvent (atoms (text solventText))
                                     content (read-string (second (re-matches (re-pattern "(\\d+\\.{0,1}\\d+).*") (text contentText))))]
                                 (if (<= content 0.01)
                                   (config! myframe :content (XChartPanel. (getGBEvsSizeChart solute solvent content)))
                                   (text! logs (str (text logs) "\n  GB Energy vs grain size: content<=0.01 required for dilute solution here")))
                                 (config! myframe :visible? true))))

(listen GBCompositionBtn :action (fn [e] (let [solute (atoms (text soluteText))
                                               solvent (atoms (text solventText))]
                                           (config! myframe :content (XChartPanel. (getGBCompositionChart solute solvent (read-string (second (re-matches (re-pattern "(\\d+\\.{0,1}\\d+).*") (text grainSizeValueText)))))))
                                           (config! myframe :visible? true))))

(listen GBCompositionVsSizeBtn :action (fn [e] (let [solute (atoms (text soluteText))
                                                     solvent (atoms (text solventText))
                                                     content (read-string (second (re-matches (re-pattern "(\\d+\\.{0,1}\\d+).*") (text contentText))))]
                                                 (config! myframe :content (XChartPanel. (getGBCompositionVsSizeChart solute solvent content)))
                                                 (config! myframe :visible? true))))

(listen GBSTableB :action (fn [e]
                            (do
                              (with-open [writer (-> (str "GB_Segregation_Enthalpy.txt") FileOutputStream. BufferedOutputStream. PrintWriter.)]
                                (.println writer "element\tAg\tCu\tZn\tFe\tC\tAl\tMg\tNi\tN\tH\tY\tCr\tSr\tSc\tTi\tV\tMo\tCo\tLi\tNa\tGa\tIn\tTl\tSn\tPb\tSb\tBi\tPd\tAu\tMn\tZr\tNb\tTc\tTa\tW\tPt\tLa\tRe\tRh\tRu\tGd\tCa\tB\tCd")
                                (doseq [rowid (range 44)]
                                  (let [a (nth atomSets rowid)]
                                    (update-at! GBSTable rowid [(:name a) (format "%.1f" (GBEnthalpy Ag a)) (format "%.1f" (GBEnthalpy Cu a)) (format "%.1f" (GBEnthalpy Zn a)) (format "%.1f" (GBEnthalpy Fe a)) (format "%.1f" (GBEnthalpy C a)) (format "%.1f" (GBEnthalpy Al a)) (format "%.1f" (GBEnthalpy Mg a)) (format "%.1f" (GBEnthalpy Ni a)) (format "%.1f" (GBEnthalpy N a)) (format "%.1f" (GBEnthalpy H a)) (format "%.1f" (GBEnthalpy Y a)) (format "%.1f" (GBEnthalpy Cr a)) (format "%.1f" (GBEnthalpy Sr a)) (format "%.1f" (GBEnthalpy Sc a)) (format "%.1f" (GBEnthalpy Ti a)) (format "%.1f" (GBEnthalpy V a)) (format "%.1f" (GBEnthalpy Mo a)) (format "%.1f" (GBEnthalpy Co a)) (format "%.1f" (GBEnthalpy Li a)) (format "%.1f" (GBEnthalpy Na a)) (format "%.1f" (GBEnthalpy Ga a)) (format "%.1f" (GBEnthalpy In a)) (format "%.1f" (GBEnthalpy Tl a)) (format "%.1f" (GBEnthalpy Sn a)) (format "%.1f" (GBEnthalpy Pb a)) (format "%.1f" (GBEnthalpy Sb a)) (format "%.1f" (GBEnthalpy Bi a)) (format "%.1f" (GBEnthalpy Pd a)) (format "%.1f" (GBEnthalpy Au a)) (format "%.1f" (GBEnthalpy Mn a)) (format "%.1f" (GBEnthalpy Zr a)) (format "%.1f" (GBEnthalpy Nb a)) (format "%.1f" (GBEnthalpy Tc a)) (format "%.1f" (GBEnthalpy Ta a)) (format "%.1f" (GBEnthalpy W a)) (format "%.1f" (GBEnthalpy Pt a)) (format "%.1f" (GBEnthalpy La a)) (format "%.1f" (GBEnthalpy Re a)) (format "%.1f" (GBEnthalpy Rh a)) (format "%.1f" (GBEnthalpy Ru a)) (format "%.1f" (GBEnthalpy Gd a)) (format "%.1f" (GBEnthalpy Ca a)) (format "%.1f" (GBEnthalpy B a)) (format "%.1f" (GBEnthalpy Cd a))])
                                    (.println writer (str (:name a) "\t" (format "%.1f" (GBEnthalpy Ag a)) "\t" (format "%.1f" (GBEnthalpy Cu a)) "\t" (format "%.1f" (GBEnthalpy Zn a)) "\t" (format "%.1f" (GBEnthalpy Fe a)) "\t" (format "%.1f" (GBEnthalpy C a)) "\t" (format "%.1f" (GBEnthalpy Al a)) "\t" (format "%.1f" (GBEnthalpy Mg a)) "\t" (format "%.1f" (GBEnthalpy Ni a)) "\t" (format "%.1f" (GBEnthalpy N a)) "\t" (format "%.1f" (GBEnthalpy H a)) "\t" (format "%.1f" (GBEnthalpy Y a)) "\t" (format "%.1f" (GBEnthalpy Cr a)) "\t" (format "%.1f" (GBEnthalpy Sr a)) "\t" (format "%.1f" (GBEnthalpy Sc a)) "\t" (format "%.1f" (GBEnthalpy Ti a)) "\t" (format "%.1f" (GBEnthalpy V a)) "\t" (format "%.1f" (GBEnthalpy Mo a)) "\t" (format "%.1f" (GBEnthalpy Co a)) "\t" (format "%.1f" (GBEnthalpy Li a)) "\t" (format "%.1f" (GBEnthalpy Na a)) "\t" (format "%.1f" (GBEnthalpy Ga a)) "\t" (format "%.1f" (GBEnthalpy In a)) "\t" (format "%.1f" (GBEnthalpy Tl a)) "\t" (format "%.1f" (GBEnthalpy Sn a)) "\t" (format "%.1f" (GBEnthalpy Pb a)) "\t" (format "%.1f" (GBEnthalpy Sb a)) "\t" (format "%.1f" (GBEnthalpy Bi a)) "\t" (format "%.1f" (GBEnthalpy Pd a)) "\t" (format "%.1f" (GBEnthalpy Au a)) "\t" (format "%.1f" (GBEnthalpy Mn a)) "\t" (format "%.1f" (GBEnthalpy Zr a)) "\t" (format "%.1f" (GBEnthalpy Nb a)) "\t" (format "%.1f" (GBEnthalpy Tc a)) "\t" (format "%.1f" (GBEnthalpy Ta a)) "\t" (format "%.1f" (GBEnthalpy W a)) "\t" (format "%.1f" (GBEnthalpy Pt a)) "\t" (format "%.1f" (GBEnthalpy La a)) "\t" (format "%.1f" (GBEnthalpy Re a)) "\t" (format "%.1f" (GBEnthalpy Rh a)) "\t" (format "%.1f" (GBEnthalpy Ru a)) "\t" (format "%.1f" (GBEnthalpy Gd a)) "\t" (format "%.1f" (GBEnthalpy Ca a)) "\t" (format "%.1f" (GBEnthalpy B a)) "\t" (format "%.1f" (GBEnthalpy Cd a))))
                                    )))
                              (config! myframe :content (scrollable GBSTable))
                              (config! myframe :visible? true))))


(listen GBExcessBtn :action(fn [e] (let [content (read-string (second (re-matches (re-pattern "(\\d+\\.{0,1}\\d+).*") (text contentText))))
                                         size (read-string (second (re-matches (re-pattern "(\\d+\\.{0,1}\\d{0,}).*") (text grainSizeValueText))))]
                                     (with-open [writer (-> (str "GB_Excess_content=" content "_size=" size  "nm.txt") FileOutputStream. BufferedOutputStream. PrintWriter.)]
                                       (.println writer (str "Xb=" content))
                                       (.println writer (str "Grain size=" size "nm"))
                                       (.println writer "solute\tsolvent\tRadius Difference(angstrom)\tfig\tXc\tXig\tExcess Gamma\tHseg")
                                       (dorun (for [a atomSets b atomSets]
                                                (let [hseg (GBEnthalpy a b)
                                                      McComp (McLeanComp content (* size 1.0e-9), 0.5e-9, 3.0, 298, hseg)
                                                      fig (:fig McComp)
                                                      Xc (:Xb McComp)
                                                      Xig (:Xig McComp)
                                                      Gamma (-> 0.5e-9 (* Xig) (/ (-> Math/PI (* 4.0) (/ 3.0) (* (Math/pow (:atomicradius b) 3.0)))))
                                                      DeltaR (-> (:atomicradius b) (- (:atomicradius a)) (* 1.0e10))]
                                                  (.println writer (str (:name a) "\t" (:name b) "\t" DeltaR "\t" fig "\t" Xc "\t" Xig "\t" Gamma "\t" hseg))))))
                                     (text! logs (str (text logs) "\n GB_Excess_content=" content "_size=" size  "nm.txt saved"))
                                     )))
;;(config! myframe :visible? true)
;;(text logs)
;;(doseq [a atomSets] (map #((format "%.1f" (GBEnthalpy Ag a)))))
;;(dorun (map #(update-at! GBSTable % {:Ag (format "%.1f" (GBEnthalpy Ag Au))}) (range 3)))
;;(-> f show!)
(defn -main
"I don't do a whole lot ... yet."
[& args]
(-> f show!)
)
;;(println "Hello, World!")

