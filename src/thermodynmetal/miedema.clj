(ns thermodynmetal.miedema
  (:gen-class)
  (:import (org.knowm.xchart XChartPanel)))

(import '(java.lang Math)
        '(org.knowm.xchart XYSeries)
        '(org.knowm.xchart SwingWrapper)
        '(org.knowm.xchart XYChart)
        '(org.knowm.xchart XChartPanel)
        '(org.knowm.xchart.style Styler$LegendPosition)
        '(javax.swing JComponent))
(use 'seesaw.core)
;;(use '(incanter charts core))
(comment
  (require '[org.shender.clojure-gnuplot :as gnuplot])
  (def gp (gnuplot/start))
  (gnuplot/exec gp
                (unset key)
                (set term qt)
                (set xrange (range 0 10))
                (set key)
                (plot (sin x) w l lw 3 t "sin(x)"))
  (gnuplot/stop gp)
  )

;;units, volume:cm^3
;;surface energy: kJ/m^2
;;modulus: Pa
;;melting point: K
;;atomic radius: m
(def Nav 6.02e23)                                           ;;Avogadro constant
(def k 1.38064852e-23)                                      ;;Boltzmann constant
(def R 8.3144598)                                           ;;Gas constant
(def c0 4.5e8)                                              ;;dimensionless semi-empirical constant
(def pi Math/PI)

(defrecord atomInfo [name, a, transition, bulkmodulus, shearmodulus, meltingpoint, surfaceenergy, phi, nws, vm, R, atomicradius])
(def Ag (->atomInfo "Ag", 0.07, true, 100.7e9, 28.65e9, 1234, 0.00125, 4.35, 2.52, 10.2, 0.15, 165e-12))
(def Cu (->atomInfo "Cu", 0.07, true, 131e9, 45.13e9, 1357.6, 0.001825, 4.45, 3.18, 7.092, 0.3, 145e-12))
(def Zn (->atomInfo "Zn", 0.1, false, 59.85e9, 37.18e9, 692.73, 0.00099, 4.1, 2.3, 9.157, 1.4, 142e-12))
(def Fe (->atomInfo "Fe", 0.04, true, 168.3e9, 81.52e9, 1809, 0.002475, 4.93, 5.55, 7.1, 1, 156e-12))
(def C (->atomInfo "C", 0.04, false, 545.4e9, 451.3e9, 4100, 0, 6.24, 5.55, 3.2, 2.1, 67e-12))
(def Al (->atomInfo "Al", 0.07, false, 72.18e9, 26.59e9, 933.25, 0.00116, 4.2, 2.7, 9.993, 1.9, 118e-12))
(def Mg (->atomInfo "Mg", 0.1, false, 35.4e9, 17.36e9, 922, 0.00076, 3.45, 1.6, 14, 0.4, 145e-12))
(def Ni (->atomInfo "Ni", 0.04, true, 186.4e9, 75.05e9, 1726, 0.0245, 5.20, 5.36, 6.6, 1, 149e-12))
(def N (->atomInfo "N", 0.04, false, 0, 0, 63.14, 0, 6.86, 4.49, 4.1, 2.3, 56e-12))
(def H (->atomInfo "H", 0.14, false, 0, 0, 14.025, 0, 5.2, 3.38, 1.7, 3.9, 53e-12))
(def Y (->atomInfo "Y", 0.07, true, 36.62e9, 25.8e9, 1799, 0.001125, 3.2, 1.77, 19.9, 0.7, 212e-12))
(def Cr (->atomInfo "Cr", 0.04, true, 190.3e9, 116.7e9, 2130, 0.0023, 4.65, 5.18, 7.2, 1.0, 166e-12))
(def Sr (->atomInfo "Sr", 0.1, true, 11.62e9, 5.229e9, 1041, 0.00041, 2.4, 0.59, 33.9, 0.4, 219e-12))
(def Sc (->atomInfo "Sc", 0.07, true, 57.9e9, 31.29e9, 1812, 0.001275, 3.25, 2.05, 15, 0.7, 184e-12))
(def Ti (->atomInfo "Ti", 0.04, true, 105.2e9, 39.34e9, 1943, 0.0021, 3.8, 3.51, 10.6, 1.0, 176e-12))
(def V (->atomInfo "V", 0.04, true, 162e9, 46.5e9, 2175, 0.00255, 4.25, 4.41, 8.4, 1.0, 171e-12))
(def Mo (->atomInfo "Mo", 0.04, true, 272.6e9, 115.8e9, 2890, 0.003, 4.65, 5.55, 9.4, 1.0, 190e-12))
(def Co (->atomInfo "Co", 0.04, true, 191.5e9, 76.42e9, 1768, 0.00255, 5.1, 5.36, 6.7, 1.0, 152e-12))
(def Li (->atomInfo "Li", 0.14, false, 11.58e9, 4.228e9, 453.7, 0.000525, 2.85, 0.94, 13.0, 0, 167e-12))
(def Na (->atomInfo "Na", 0.14, false, 6.817e9, 3.434e9, 371, 0.00026, 2.7, 0.55, 23.8, 0, 190e-12))
(def Ga (->atomInfo "Ga", 0.07, false, 56.9e9, 37.47e9, 302.9, 0.0011, 4.1, 2.25, 11.8, 1.9, 136e-12))
(def In (->atomInfo "In", 0.07, false, 41.09e9, 3.728e9, 429.7, 0.000675, 3.9, 1.6, 15.7, 1.9, 156e-12))
(def Tl (->atomInfo "Tl", 0.07, false, 35.93e9, 2.747e9, 577, 0.000575, 3.9, 1.4, 17.2, 1.9, 156e-12))
(def Sn (->atomInfo "Sn", 0.04, false, 110.9e9, 18.44e9, 505.06, 0.000675, 4.15, 1.9, 16.3, 2.1, 145e-12))
(def Pb (->atomInfo "Pb", 0.04, false, 42.99e9, 5.396e9, 600.6, 0.0006, 4.1, 1.52, 18.3, 2.1, 154e-12))
(def Sb (->atomInfo "Sb", 0.04, false, 38.29e9, 20.01e9, 904, 0.000535, 4.4, 2, 16.9, 2.3, 133e-12))
(def Bi (->atomInfo "Bi", 0.04, false, 31.48e9, 12.85e9, 544.52, 0.00049, 4.15, 1.56, 19.3, 2.3, 143e-12))
(def Pd (->atomInfo "Pd", 0.04, true, 180.9e9, 51.11e9, 1825, 0.00205, 5.45, 4.66, 8.9, 1.0, 169e-12))
(def Au (->atomInfo "Au", 0.07, true, 173.2e9, 27.57e9, 137.58, 0.0015, 5.15, 3.87, 10.2, 0.3, 174e-12))
(def Mn (->atomInfo "Mn", 0.04, true, 59.67e9, 76.52e9, 1517, 0.0016, 4.45, 4.17, 7.3, 1.0, 161e-12))
(def Zr (->atomInfo "Zr", 0.04, true, 83.335e9, 34.14e9, 2125, 0.002, 3.45, 2.8, 14.0, 1.0, 206e-12))
(def Nb (->atomInfo "Nb", 0.04, true, 170.3e9, 37.47e9, 2740, 0.0027, 4.05, 4.41, 10.8, 1.0, 198e-12))
(def Tc (->atomInfo "Tc", 0.04, true, 297.2e9, 142.2e9, 2473, 0.00315, 5.3, 5.93, 8.6, 1.0, 183e-12))
(def Ta (->atomInfo "Ta", 0.04, true, 200.1e9, 28.67e9, 3287, 0.003150, 4.05, 4.33, 10.8, 1.0, 200e-12))
(def W (->atomInfo "W", 0.04, true, 323.3e9, 153e9, 3680, 0.003675, 4.8, 5.93, 9.5, 1.0, 193e-12))
(def Pt (->atomInfo "Pt", 0.04, true, 278.4e9, 61.02e9, 2045, 0.002475, 5.65, 5.64, 9.1, 1.0, 177e-12))
(def La (->atomInfo "La", 0.07, true, 24.3e9, 14.91e9, 1193, 0.001020, 3.17, 1.64, 22.5, 0.7, 226e-12))
(def Re (->atomInfo "Re", 0.04, true, 371.8e9, 178.5e9, 3453, 0.0036, 5.2, 6.33, 8.8, 1.0, 188e-12))
(def Rh (->atomInfo "Rh", 0.04, true, 270.6e9, 147.2e9, 2236, 0.0027, 5.4, 5.45, 8.3, 1.0, 173e-12))
(def Ru (->atomInfo "Ru", 0.04, true, 320.9e9, 159.9e9, 2523, 0.00305, 5.4, 6.13, 8.2, 1.0, 178e-12))
(def Gd (->atomInfo "Gd", 0.07, true, 38.34e9, 22.27e9, 1585, 0.00111, 3.2, 1.77, 19.9, 0.7, 233e-12))
(def Ca (->atomInfo "Ca", 0.1, false, 15.21e9, 7.358e9, 1112, 0.00049, 2.55, 0.75, 26.2, 0.4, 194e-12))
(def B (->atomInfo "B", 0.07, false, 178.5e9, 203.1e9, 2300, 0.00305, 5.30, 5.36, 4.7, 1.9, 87e-12))
(def Cd (->atomInfo "Cd", 0.1, false, 46.75e9, 24.13e9, 594.18, 0.00074, 4.05, 1.91, 13.0, 1.4, 161e-12))


(defn PQR [atoma atomb] (cond
                          (and (:transition atoma) (:transition atomb)) {:P 14.1, :Q 132.54, :R 0}
                          (not (or (:transition atoma) (:transition atomb))) {:P 10.6, :Q 99.64, :R 0}
                          (and (or (:transition atoma) (:transition atomb)) (not (and (:transition atoma) (:transition atomb)))) {:P 12.3, :Q 115.62, :R (* 12.3 (:R atoma) (:R atomb))}
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
                                                                va1 (Math/pow (* (Math/pow (:vm solute) (/ 2.0 3.0)) (+ 1 (* (:a solute) (* cbs1 (+ 1 (* 8 cas1 cas1 cbs1 cbs1))) (- (:phi solute) (:phi solvent))))) (/ 3.0 2.0))
                                                                vb1 (Math/pow (* (Math/pow (:vm solvent) (/ 2.0 3.0)) (+ 1 (* (:a solvent) (* cas1 (+ 1 (* 8 cas1 cas1 cbs1 cbs1))) (- (:phi solvent) (:phi solute))))) (/ 3.0 2.0))
                                                                ]
                                                            [va1 vb1]
                                                            )) (let [cas (* ca (Math/pow (:vm solute) (/ 2.0 3.0)) (/ 1.0 (+ (* ca (Math/pow (:vm solute) (/ 2.0 3.0))) (* (- 1 ca) (Math/pow (:vm solvent) (/ 2.0 3.0))))))
                                                                     cbs (- 1 cas)
                                                                     ]
                                                                 [(Math/pow (* (Math/pow (:vm solute) (/ 2.0 3.0)) (+ 1 (* (:a solute) (* cbs (+ 1 (* 8 cas cas cbs cbs))) (- (:phi solute) (:phi solvent))))) (/ 3.0 2.0))
                                                                  (Math/pow (* (Math/pow (:vm solvent) (/ 2.0 3.0)) (+ 1 (* (:a solvent) cas (+ 1 (* 8 cas cas cbs cbs)) (- (:phi solvent) (:phi solute))))) (/ 3.0 2.0))
                                                                  ])
                                                        )) 19))
;;test
;;(valloy 0.5 Cu Zn)
;;(valloy 0.5 Zn Cu)



(defn formationEnthalpy [ca solute solvent]                 ;;ca is the concentration of solute
  (let [va (first (valloy ca solute solvent))
        vb (second (valloy ca solute solvent))
        cas (* ca (Math/pow va (/ 2.0 3.0)) (/ 1.0 (+ (* ca (Math/pow va (/ 2.0 3.0))) (* (- 1 ca) (Math/pow vb (/ 2.0 3.0))))))
        cbs (- 1 cas)
        fc (* cbs (+ 1 (* 8 cas cas cbs cbs)))
        ;vb (Math/pow (* (Math/pow (:vm solvent) (/ 2.0 3.0)) (+ 1 (* (:a solvent) cas (+ 1 (* 8 cas cas cbs cbs)) (- (:phi solvent) (:phi solute))))) (/ 3.0 2.0))
        pqr (PQR solute solvent)
        ]
    (* 2 fc ca
       (/ (Math/pow va (/ 2.0 3.0))
          (+ (Math/pow (:nws solute) (/ -1.0 3.0))
             (Math/pow (:nws solvent) (/ -1.0 3.0))))
       (+ (* -1 (:P pqr) (Math/pow (- (:phi solute) (:phi solvent)) 2))
          (* (:Q pqr) (Math/pow (- (Math/pow (:nws solute) (/ 1.0 3.0)) (Math/pow (:nws solvent) (/ 1.0 3.0))) 2))
          (* -1 (:R pqr))
          )
       )))

(defn Eshelby [solute solvent]
  (-> 24 (* pi
            (:bulkmodulus solvent)
            (:shearmodulus solute)
            (:atomicradius solute)
            (:atomicradius solvent)
            (Math/pow (- (:atomicradius solute) (:atomicradius solvent)) 2.0))
      (/ (+ (* 3 (:bulkmodulus solvent) (:atomicradius solvent)) (* 4 (:shearmodulus solute) (:atomicradius solute))))))

(defn GBEnthalpy [solute solvent] (+
                                    (-> (Eshelby solute solvent) (* Nav) (/ 1000))
                                    (* -0.71 (/ 1.0 3.0) (/ 1.0 2.0)
                                       (- (* c0 (:surfaceenergy solute) (Math/pow (* (:vm solute) 1.0e-6) (/ 2.0 3.0)))
                                          (* c0 (:surfaceenergy solvent) (Math/pow (* (:vm solvent) 1.0e-6) (/ 2.0 3.0)))
                                          (heatOfSolution solute solvent)))))

;;Mclean Segregation law
(defn McLean
  "Mclean Segregation law, which determines the concentration of solute in the grain boundary. Return the concentration of solute in the grain boundary. Note that the segregation enthalpy should use positive values here."
  [temperature, Hseg, Xbulk]
  {:pre [(>= Xbulk 0) (<= Xbulk 1) (>= temperature 0)]}
  (->> (-> Xbulk (/ (- 1 Xbulk)) (* (Math/exp (/ Hseg k temperature)))) (/ 1.0) (+ 1.0) (/ 1.0)))

;;Solve compositions in the grain interial and boundary
(defn
  McLeanComp
  "Solve compositions in the grain interial and boundary. By the forumlae of (1) Mclean segregation law:Xig/(1-Xig)=Xb/(1-Xb)*exp(Hseg/kT) and (2) X=(1-fig)*Xb+fig*Xig. Where X is the global composition, Xb is the composition in the grain interial, Xig is the composition in the boundary, fig is the volume fraction of the boundary layer, which could be expressed as: fig=1-((d-t)/d)^D, where D is the dimensionality parameter. D=3 should be used for a general threedimensional polycrystal, while D=2 is useful for columnar or highly elongated grain structures and D=1 applies to lamellar or platelike grains."
  [globalComposition, grainSize, boundaryThickness, Dimensionality, temperature, Hseg]
  {:pre [(>= globalComposition 0) (<= globalComposition 1) (< boundaryThickness grainSize) (>= boundaryThickness 0)
         (>= Dimensionality 0) (<= Dimensionality 3) (>= temperature 0)]}
  (let [X globalComposition,
        d grainSize,
        t boundaryThickness,
        D Dimensionality,
        T temperature,
        H (* Hseg 1000.0)                                   ;;since the unit of Hseg is kJ/mol, here we need J/mol
        fig (- 1 (Math/pow (-> d (- t) (/ d)) D))
        ex (Math/exp (-> -1 (* Hseg) (/ R T)))
        a (* fig (- ex 1))
        b (-> 1 (- ex) (* fig) (+ (-> 1 (- X) (* ex))) (+ X))
        c (* -1.0 X)
        Xig (-> -1 (* b) (+ (Math/sqrt (-> b (* b) (- (* 4.0 a c))))) (/ 2.0 a))
        Xb (-> X (- (* fig Xig)) (/ (- 1 fig)))] {:X X :Xig Xig :Xb Xb :fig fig}))


;;unit:J/m^2
(defn GBEnergy "Grain boundary segregation and thermodynamically stable binary nanocrystalline alloys, Jason R. Trelewicz and Christopher A. Schuh. PHYSICAL REVIEW B 79, 094112 (2009), http://dx.doi.org/10.1103/PhysRevB.79.094112  Eq. 25(b), note that the atomic volume is of the solvent"
  [gA, boundaryThickness, Xig, atomicVolume, Hseg, temperature, Xb]
  (-> gA (- (-> boundaryThickness (* Xig) (/ atomicVolume) (* (+ (/ (* Hseg 1000.0) Nav) (* k temperature (Math/log Xb))))))))



;;test GBEnergy
(comment
  (GBEnergy 0 0.5e-9 0.00100398 (-> Math/PI (* 4.0) (/ 3.0) (* (Math/pow (:atomicradius Cu) 3.0))) 10 298 0.00099999)
  (-> 0 (- (-> 0.5e-9 (* 0.00100398) (/ 1.277e-29) (* (+ (/ (* 10 1000.0) 6.02e23) (* 1.038e-23 298 (Math/log 0.00099999)))))))
  (-> Math/PI (* 4.0) (/ 3.0) (* (Math/pow (:atomicradius Cu) 3.0)))
  (McLeanComp 0.01 100.0e-6, 0.5e-9, 3.0, 300, 10)
  (map #(McLeanComp % 100.0e-6, 0.5e-9, 3.0, 298, 10) [0.01 0.02 0.03]))

(defn GBEnergy1
  "Equation (1) in H.A. Murdoch, C.A. Schuh, Estimation of grain boundary segregation enthalpy and its role in stable nanocrystalline alloy design, Journal of Materials Research. 28 (2013) 2154–2163. doi:10.1557/jmr.2013.211."
  [g0 soluteExcess segregationEnthalpy Temperature soluteContent]
  (- g0 (* soluteExcess (- segregationEnthalpy (* k Temperature (Math/log soluteContent))))))

(def cwidth 888)
(def cheight 666)

(defn getChart [solute solvent]
  (let [mychart (XYChart. cwidth cheight)
        ;;sw (SwingWrapper. mychart)
        xdata (take 201 (iterate #(+ % (/ 1.0 200.0)) 0))
        ydata (map #(formationEnthalpy % solute solvent) xdata)]
    (.setTitle mychart (str "Miedema Formation Enthalpy " (:name solvent) "-" (:name solute)))
    (.setXAxisTitle mychart (str (:name solute) "(at.%)"))
    (.setYAxisTitle mychart "Formation Enthalpy (kJ/mol)")
    (.setLegendPosition (.getStyler mychart) Styler$LegendPosition/InsideSW)
    (.addSeries mychart (str (:name solvent) "-" (:name solute)), xdata, ydata)
    ;;(.displayChart sw)
    (println (str "solvent-solute(" (:name solvent) "-" (:name solute) ")"))
    (println "Heat of solution(kJ/mol):")
    (println (str (:name solute) " in " (:name solvent) ":" (heatOfSolution solute solvent)))
    (println (str (:name solvent) " in " (:name solute) ":" (heatOfSolution solvent solute)))
    (println "Grain boundary segregation enthalpy(kJ/mol):")
    (println (GBEnthalpy solute solvent))
    mychart
    ))


;; (GBEnergy gA, boundaryThickness, Xig, atomicVolume, Hseg, temperature, Xb)
;; (McLeanComp globalComposition, grainSize, boundaryThickness, Dimensionality, temperature, Hseg)
(defn getGBEChart [solute solvent grainsize]
  {:pre [(> grainsize 1)]}
  (let [mychart (XYChart. cwidth cheight)
        ;;sw (SwingWrapper. mychart)
        xdata (take 201 (iterate #(+ % (/ 0.01 200.0)) 0))
        hseg (GBEnthalpy solute solvent)
        mcleancomp (map #(McLeanComp % (* grainsize 1.0e-9), 0.5e-9, 3.0, 298, hseg) xdata)
        xig (map #(:Xig %) mcleancomp)
        xb (map #(:Xb %) mcleancomp)
        ydata (map #(GBEnergy 0 0.5e-9 %1 (-> Math/PI (* 4.0) (/ 3.0) (* (Math/pow (:atomicradius solvent) 3.0))) hseg 298 %2) xig xb)] ;;todo,what is the grain boundary energy of an pure metal?? need solution
    (.setTitle mychart (str "Grain boundary energy " (:name solvent) "-" (:name solute) " @T=298 K,d=" grainsize " nm"))
    (.setXAxisTitle mychart (str (:name solute) "(at.%)"))
    (.setYAxisTitle mychart (str "GB engergy relative to pure " (:name solvent) " GB (J/m^2)"))
    (.setLegendPosition (.getStyler mychart) Styler$LegendPosition/InsideNE)
    (.addSeries mychart (str (:name solvent) "-" (:name solute)), xdata, ydata)
    mychart
    ))

;;variation of grain boundary energy with grain size
(defn getGBEvsSizeChart [solute solvent content]
  {:pre [(<= content 0.01) (> content 0)]}
  (let [mychart (XYChart. cwidth cheight)
        ;;sw (SwingWrapper. mychart)
        xdata (range 1 300)                                 ;;grain size unit: nm
        hseg (GBEnthalpy solute solvent)
        mcleancomp (map #(McLeanComp content (* % 1.0e-9), 0.5e-9, 3.0, 298, hseg) xdata)
        xig (map #(:Xig %) mcleancomp)
        xb (map #(:Xb %) mcleancomp)
        ydata (map #(GBEnergy 0 0.5e-9 %1 (-> Math/PI (* 4.0) (/ 3.0) (* (Math/pow (:atomicradius solvent) 3.0))) hseg 298 %2) xig xb)] ;;todo,what is the grain boundary energy of an pure metal?? need solution

    (.setTitle mychart (str "Grain boundary energy " (:name solvent) "-" (:name solute) " @T=298 K content=" (format "%.3fat.%%" (* 100.0 content))))
    (.setXAxisTitle mychart "Grain size(nm)")
    (.setYAxisTitle mychart (str "GB engergy relative to pure " (:name solvent) " GB(J/m^2)"))
    (.setLegendPosition (.getStyler mychart) Styler$LegendPosition/InsideNE)
    (.addSeries mychart (str (:name solvent) "(1-x)" (:name solute) "(x), x=" content), xdata, ydata)
    mychart
    ))

(defn getGBCompositionChart [solute solvent grainsize]
  {:pre [(> grainsize 1.0)]}
  (let [mychart (XYChart. cwidth cheight)
        ;;sw (SwingWrapper. mychart)
        xdata (take 201 (iterate #(+ % (/ 0.1 200.0)) 0))   ;;grain size unit: nm
        hseg (GBEnthalpy solute solvent)
        mcleancomp (map #(McLeanComp % (* grainsize 1.0e-9), 0.5e-9, 3.0, 298, hseg) xdata)
        xig (map #(:Xig %) mcleancomp)
        xb (map #(:Xb %) mcleancomp)
        ydata (map #(- %1 %2) xig xb)]
    (.setTitle mychart (str "Composition variation " (:name solvent) "-" (:name solute) " @T=298 K d=" (format "%.0f nm" (* grainsize 1.0))))
    (.setXAxisTitle mychart "Global composition(at.%)")
    (.setYAxisTitle mychart "Composition difference")
    (.setLegendPosition (.getStyler mychart) Styler$LegendPosition/InsideNW)
    (.addSeries mychart "Xig-Xb", xdata, ydata)
    ;;return mychart
    mychart
    ))

(defn getGBCompositionVsSizeChart [solute solvent content]
  {:pre [(> content 0) (< content 1)]}
  (let [mychart (XYChart. cwidth cheight)
        ;;sw (SwingWrapper. mychart)
        xdata (range 1 300)                                 ;;grain size unit: nm
        hseg (GBEnthalpy solute solvent)
        mcleancomp (map #(McLeanComp content (* % 1.0e-9), 0.5e-9, 3.0, 298, hseg) xdata)
        xig (map #(:Xig %) mcleancomp)
        xb (map #(:Xb %) mcleancomp)
        ydata (map #(- %1 %2) xig xb)]
    (.setTitle mychart (str "Composition variation " (:name solvent) "-" (:name solute) " @T=298 K content=" (format "%.3fat.%%" (* 100.0 content))))
    (.setXAxisTitle mychart "grain size(nm)")
    (.setYAxisTitle mychart "Composition difference")
    (.setLegendPosition (.getStyler mychart) Styler$LegendPosition/InsideNW)
    (.addSeries mychart "Xig-Xb", xdata, ydata)
    ;;return mychart
    mychart
    ))

(defn plot [solute solvent] (let [myframe (frame :title "Miedema enthalpy" :height cheight :width cwidth)]
                              (config! myframe :content (XChartPanel. (getChart solute solvent)))
                              (-> myframe show!)))




;;(println " Zn in Cu:" (heatOfSolution Zn Cu) "\n Cu in Zn:" (heatOfSolution Cu Zn))
;;(println " Zn in Cu:" (formationEnthalpy 0.25 Zn Cu) "\n Cu in Zn:" (formationEnthalpy 0.75 Cu Zn))

;;(doseq [elem [Ag Zn Al Bi Mn Fe Ni]] (println (:name elem) (heatOfSolution elem Cu)))


;;(doseq [elem [Cu Zn Al Ag Ni Mg Fe Bi Mn Au Zr W Pd Pb Cr]] (println "Vm" (:name elem) (:vm elem)))
 
