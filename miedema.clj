(import '(java.lang Math)
'(org.knowm.xchart XYSeries)
  '(org.knowm.xchart SwingWrapper)
  '(org.knowm.xchart XYChart)
  '(org.knowm.xchart.style Styler$LegendPosition)
  '(javax.swing JComponent))

(def k 1.38064852e-23) ;;Boltzmann constant
(def R 8.3144598)  ;;Gas constant

(defrecord atomInfo [name,a,transition,bulkmodulus,shearmodulus,meltingpoint,surfaceenergy,phi,nws,vm,R])
	(def Ag (->atomInfo "Ag",0.07,true,100.7e9,28.65e9,1234,0.00125,4.35,2.52,10.2,0.15))
	(def Cu (->atomInfo "Cu",0.07,true,131e9,45.13e9,1357.6,0.001825,4.45,3.18,7.092,0.3))
	(def Zn (->atomInfo "Zn",0.1,false,59.85e9,37.18e9,692.73,0.00099,4.1,2.3,9.157,1.4))
	(def Fe (->atomInfo "Fe",0.04,true,168.3e9,81.52e9,1809,0.002475,4.93,5.55,7.1,1))
	(def C (->atomInfo "C",0.04,false,545.4e9,451.3e9,4100,0,6.24,5.55,3.2,2.1))
	(def Al (->atomInfo "Al",0.07,false,72.18e9,26.59e9,933.25,0.00116,4.2,2.7,9.993,1.9))
	(def Mg (->atomInfo "Mg",0.1,false,35.4e9,17.36e9,922,0.00076,3.45,1.6,14,0.4))
	(def Ni (->atomInfo "Ni",0.04,true,186.4e9,75.05e9,1726,0.0245,5.20,5.36,6.6,1))
	(def N (->atomInfo "N",0.04,false,0,0,63.14,0,6.86,4.49,4.1,2.3))
	(def H (->atomInfo "H",0.14,false,0,0,14.025,0,5.2,3.38,1.7,3.9))
	(def Y (->atomInfo "Y",0.07,true,36.62e9,25.8e9,1799,0.001125,3.2,1.77,19.9,0.7))
	(def Cr (->atomInfo "Cr",0.04,true,190.3e9,116.7e9,2130,0.0023,4.65,5.18,7.2,1.0))
	(def Sr (->atomInfo "Sr",0.1,true,11.62e9,5.229e9,1041,0.00041,2.4,0.59,33.9,0.4))
	(def Sc (->atomInfo "Sc",0.07,true,57.9e9,31.29e9,1812,0.001275,3.25,2.05,15,0.7))
	(def Ti (->atomInfo "Ti",0.04,true,105.2e9,39.34e9,1943,0.0021,3.8,3.51,10.6,1.0))
	(def V (->atomInfo "V",0.04,true,162e9,46.5e9,2175,0.00255,4.25,4.41,8.4,1.0))
	(def Mo (->atomInfo "Mo",0.04,true,272.6e9,115.8e9,2890,0.003,4.65,5.55,9.4,1.0))
	(def Co (->atomInfo "Co",0.04,true,191.5e9,76.42e9,1768,0.00255,5.1,5.36,6.7,1.0))
	(def Li (->atomInfo "Li",0.14,false,11.58e9,4.228e9,453.7,0.000525,2.85,0.94,13.0,0))
	(def Na (->atomInfo "Na",0.14,false,6.817e9,3.434e9,371,0.00026,2.7,0.55,23.8,0))
	(def Ga (->atomInfo "Ga",0.07,false,56.9e9,37.47e9,302.9,0.0011,4.1,2.25,11.8,1.9))
	(def In (->atomInfo "In",0.07,false,41.09e9,3.728e9,429.7,0.000675,3.9,1.6,15.7,1.9))
	(def Tl (->atomInfo "Tl",0.07,false,35.93e9,2.747e9,577,0.000575,3.9,1.4,17.2,1.9))
	(def Sn (->atomInfo "Sn",0.04,false,110.9e9,18.44e9,505.06,0.000675,4.15,1.9,16.3,2.1))
	(def Pb (->atomInfo "Pb",0.04,false,42.99e9,5.396e9,600.6,0.0006,4.1,1.52,18.3,2.1))
	(def Sb (->atomInfo "Sb",0.04,false,38.29e9,20.01e9,904,0.000535,4.4,2,16.9,2.3))
	(def Bi (->atomInfo "Bi",0.04,false,31.48e9,12.85e9,544.52,0.00049,4.15,1.56,19.3,2.3))
	(def Pd (->atomInfo "Pd",0.04,true,180.9e9,51.11e9,1825,0.00205,5.45,4.66,8.9,1.0))
	(def Au (->atomInfo "Au",0.07,true,173.2e9,27.57e9,137.58,0.0015,5.15,3.87,10.2,0.3))
	(def Mn (->atomInfo "Mn",0.04,true,59.67e9,76.52e9,1517,0.0016,4.45,4.17,7.3,1.0))
	(def Zr (->atomInfo "Zr",0.04,true,83.335e9,34.14e9,2125,0.002,3.45,2.8,14.0,1.0))
	(def Nb (->atomInfo "Nb",0.04,true,170.3e9,37.47e9,2740,0.0027,4.05,4.41,10.8,1.0))
	(def Tc (->atomInfo "Tc",0.04,true,297.2e9,142.2e9,2473,0.00315,5.3,5.93,8.6,1.0))
	(def Ta (->atomInfo "Ta",0.04,true,200.1e9,28.67e9,3287,0.003150,4.05,4.33,10.8,1.0))
	(def W (->atomInfo "W",0.04,true,323.3e9,153e9,3680,0.003675,4.8,5.93,9.5,1.0))
	(def Pt (->atomInfo "Pt",0.04,true,278.4e9,61.02e9,2045,0.002475,5.65,5.64,9.1,1.0))
	(def La (->atomInfo "La",0.07,true,24.3e9,14.91e9,1193,0.001020,3.17,1.64,22.5,0.7))
	(def Re (->atomInfo "Re",0.04,true,371.8e9,178.5e9,3453,0.0036,5.2,6.33,8.8,1.0))
	(def Rh (->atomInfo "Rh",0.04,true,270.6e9,147.2e9,2236,0.0027,5.4,5.45,8.3,1.0))
	(def Ru (->atomInfo "Ru",0.04,true,320.9e9,159.9e9,2523,0.00305,5.4,6.13,8.2,1.0))
	(def Gd (->atomInfo "Gd",0.07,true,38.34e9,22.27e9,1585,0.00111,3.2,1.77,19.9,0.7))
	(def Ca (->atomInfo "Ca",0.1,false,15.21e9,7.358e9,1112,0.00049,2.55,0.75,26.2,0.4))
	(def B (->atomInfo "B",0.07,false,178.5e9,203.1e9,2300,0.00305,5.30,5.36,4.7,1.9))
(def Cd (->atomInfo "Cd",0.1,false,46.75e9,24.13e9,594.18,0.00074,4.05,1.91,13.0,1.4))


(defn PQR [atoma atomb] (cond
                          (and (:transition atoma) (:transition atomb)) {:P 14.1,:Q 132.54,:R 0}
                          (not (or (:transition atoma) (:transition atomb))) {:P 10.6,:Q 99.64,:R 0}
                          (and  (or (:transition atoma) (:transition atomb)) (not (and (:transition atoma) (:transition atomb)))) {:P 12.3,:Q 115.62,:R (* 12.3 (:R atoma) (:R atomb))}
                          ))
(defn heatOfSolution [solute solvent]
                                   (* 2
                                      (/ (Math/pow (:vm solute) (/ 2.0 3.0))
                                         (+ (Math/pow (:nws solute) (/ -1.0 3.0))
                                            (Math/pow (:nws solvent) (/ -1.0 3.0))))
                                      (let [pqr (PQR solute solvent)]
                                        (+ (* -1 (:P pqr) (Math/pow (- (:phi solute) (:phi solvent)) 2))
                                           (* (:Q pqr) (Math/pow (- (Math/pow (:nws solute) (/ 1.0 3.0)) (Math/pow (:nws solvent) (/ 1.0 3.0))) 2))
                                           (* -1 (:R pqr))
                                         ))
                                      ))


(defn valloy [ca solute solvent] (nth (take 25 (iterate (fn [[va vb]]
                         (let [cas1 (* ca (Math/pow va (/ 2.0 3.0)) (/ 1.0 (+ (* ca (Math/pow va (/ 2.0 3.0))) (* (- 1 ca) (Math/pow vb (/ 2.0 3.0))))))
                               cbs1 (- 1 cas1)
                               va1 (Math/pow (* (Math/pow (:vm solute) (/ 2.0 3.0)) (+ 1 (* (:a solute)  (* cbs1 (+ 1 (* 8 cas1 cas1 cbs1 cbs1))) (- (:phi solute) (:phi solvent))))) (/ 3.0 2.0))
                               vb1 (Math/pow (* (Math/pow (:vm solvent) (/ 2.0 3.0)) (+ 1 (* (:a solvent)  (* cas1 (+ 1 (* 8 cas1 cas1 cbs1 cbs1))) (- (:phi solvent) (:phi solute))))) (/ 3.0 2.0))
                               ]
                         [va1 vb1]
                         )) (let [cas  (* ca (Math/pow (:vm solute) (/ 2.0 3.0)) (/ 1.0 (+ (* ca (Math/pow (:vm solute) (/ 2.0 3.0))) (* (- 1 ca) (Math/pow (:vm solvent) (/ 2.0 3.0))))))
                                 cbs (- 1 cas)
                                 ]
                             [(Math/pow (* (Math/pow (:vm solute) (/ 2.0 3.0)) (+ 1 (* (:a solute)  (* cbs (+ 1 (* 8 cas cas cbs cbs))) (- (:phi solute) (:phi solvent))))) (/ 3.0 2.0))
                            (Math/pow (* (Math/pow (:vm solvent) (/ 2.0 3.0)) (+ 1 (* (:a solvent) cas (+ 1 (* 8 cas cas cbs cbs)) (- (:phi solvent) (:phi solute))))) (/ 3.0 2.0))
                            ])
                                                        )) 19))
;;test
(valloy 0.5 Cu Zn)
;;(valloy 0.5 Zn Cu)



(defn formationEnthalpy [ca solute solvent];;ca is the concentration of solute
  (let [va (first (valloy ca solute solvent))
        vb (second (valloy ca solute solvent))
        cas (* ca (Math/pow va (/ 2.0 3.0)) (/ 1.0 (+ (* ca (Math/pow va (/ 2.0 3.0))) (* (- 1 ca) (Math/pow vb (/ 2.0 3.0))))))
        cbs (- 1 cas)
        fc (* cbs (+ 1 (* 8 cas cas cbs cbs)))
        ;vb (Math/pow (* (Math/pow (:vm solvent) (/ 2.0 3.0)) (+ 1 (* (:a solvent) cas (+ 1 (* 8 cas cas cbs cbs)) (- (:phi solvent) (:phi solute))))) (/ 3.0 2.0))
        pqr (PQR solute solvent)
        ]
                                   (*  2  fc ca 
                                      (/  (Math/pow va (/ 2.0 3.0)) 
                                         (+ (Math/pow (:nws solute) (/ -1.0 3.0))
                                            (Math/pow (:nws solvent) (/ -1.0 3.0))))                                      
                                        (+ (* -1 (:P pqr) (Math/pow (- (:phi solute) (:phi solvent)) 2))
                                           (* (:Q pqr) (Math/pow (- (Math/pow (:nws solute) (/ 1.0 3.0)) (Math/pow (:nws solvent) (/ 1.0 3.0))) 2))
                                           (* -1 (:R pqr))
                                         )
                                        )))

(defn GBEnergy
  "Equation (1) in H.A. Murdoch, C.A. Schuh, Estimation of grain boundary segregation enthalpy and its role in stable nanocrystalline alloy design, Journal of Materials Research. 28 (2013) 2154¨C2163. doi:10.1557/jmr.2013.211."
  [g0 soluteExcess segregationEnthalpy Temperature soluteContent]
  (- g0 (* soluteExcess (- segregationEnthalpy (* k temperature (Math/log soluteContent))))))

(defn addLabels [graphics label x y] (.drawString graphics label x y))

(def width 888)
(def height 666)
(def mychart (XYChart. width height))
(.setTitle mychart "Miedema Formation Enthalpy Cu-X")
(.setXAxisTitle mychart "solute (at.%)")
(.setYAxisTitle mychart "Formation Enthalpy (kJ/mol)")
(.setLegendPosition (.getStyler mychart) Styler$LegendPosition/InsideSW)
(def xdata (take 201 (iterate #(+ % (/ 1.0 200.0)) 0)))
(def ydata (map #(formationEnthalpy % Zn Cu) xdata))
(def ydata1 (map #(formationEnthalpy % Al Cu) xdata))
(def ydata2 (map #(formationEnthalpy % Bi Cu) xdata))
(.addSeries mychart "Cu-Zn",xdata,ydata)
(.addSeries mychart "Cu-Al" xdata,ydata1)
(.addSeries mychart "Cu-Bi" xdata,ydata2)
(def sw (SwingWrapper. mychart))
(.displayChart sw)
(def graph (.getGraphics (.getXChartPanel sw)))
(addLabels graph
           "Heat of solution(kJ/mol)"
           (- (.getWidth (.getXChartPanel sw))  150)
           (- (.getHeight (.getXChartPanel sw))  160))
(addLabels graph
           (str "Zn in Cu: " (subs (str (heatOfSolution Zn Cu)) 0 6))
           (- (.getWidth (.getXChartPanel sw))  150)
           (- (.getHeight (.getXChartPanel sw))  140))
(addLabels graph
           (str "Al in Cu: " (subs (str (heatOfSolution Al Cu)) 0 6))
           (- (.getWidth (.getXChartPanel sw))  150)
           (- (.getHeight (.getXChartPanel sw))  120))
(addLabels graph
           (str "Bi in Cu: " (subs (str (heatOfSolution Bi Cu)) 0 6))
           (- (.getWidth (.getXChartPanel sw))  150)
           (- (.getHeight (.getXChartPanel sw))  100))

;(.repaintChart sw)

(println " Zn in Cu:"(heatOfSolution Zn Cu) "\n Cu in Zn:" (heatOfSolution Cu Zn))
(println " Zn in Cu:"(formationEnthalpy 0.25 Zn Cu) "\n Cu in Zn:" (formationEnthalpy 0.75 Cu Zn))

(doseq [elem [Ag Zn Al Bi Mn Fe Ni]] (println (:name elem) (heatOfSolution elem Cu)))


(doseq [elem [Cu Zn Al Ag Ni Mg Fe Bi Mn Au Zr W Pd Pb Cr]] (println "Vm" (:name elem) (:vm elem)))
