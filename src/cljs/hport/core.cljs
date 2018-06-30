(ns hport.core
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :refer-macros [defroute]]
              [accountant.core :as accountant]))

(enable-console-print!)

(defonce current-component (atom nil))
(defonce mobile-menu-visibility (atom "hidden"))

(defn mobile? []
  (let [device (.-userAgent js/navigator)]
    (if (or
         (re-find #"Android" device)
         (re-find #"webOS" device)
         (re-find #"iPhone" device)
         (re-find #"iPod" device)
         (re-find #"BlackBerry" device)
         (re-find #"Windows Phone" device))
      true false)))

(defn toggle-visibility []
  (cond
    (= @mobile-menu-visibility "hidden") (reset! mobile-menu-visibility "visible")
    (= @mobile-menu-visibility "visible") (reset! mobile-menu-visibility "hidden")))

(defn home-content-component []
  [:div.home-content
   [:h1 "I am Julio Berina"]
   [:p "I like creating, primarily with code"]])

(defn about-content-component []
  [:div.about-content
   [:h1 "About"]
   [:p "Hi, I'm Julio Berina."]
   [:p (str "I'm a web developer and love to make things in general. "
            "My current programming interests are Javascript and React, "
            "Clojurescript and Reagent, and some Ruby here and there. "
            "Sometimes, I like to go back to my roots and do some C++ "
            ", Java, and all the other languages you learn in school. "
            "Despite primarily focusing on the web, I like to dabble in "
            "other areas as well to satisfy my curiosity.")]
   [:p (str "Besides programming I like watching documentaries, "
            "eating out, and playing basketball on most Sundays.")]])

(defn projects-content-component []
  [:div.projects-content
   [:h1 "Projects"]
   [:p (str "I've worked on web projects as well as projects in other areas of "
            "software development.  Here are some of the projects I've worked on")]
   [:br]
   [:a {:href "https://github.com/julioberina/presentation"} [:h2 "Intro to Clojure"]]
   [:p "A coded presentation to present an intro to Clojure at my local meetup"]
   [:p "Built with: Clojurescript, Reagent"] [:br]
   [:a {:href "https://github.com/julioberina/splurty"} [:h2 "Splurty"]]
   [:p "A web app that allows users to see and contribute quotes by famous people"]
   [:p "Built with: Ruby on Rails, Bootstrap"] [:br]
   [:a {:href "https://github.com/julioberina/grammable"} [:h2 "Grammable"]]
   [:p "A clone of Instagram"]
   [:p "Built with: Ruby on Rails, Bootstrap"] [:br]
   [:a {:href "https://github.com/julioberina/Kekoa"} [:h2 "KekoaVeteran.org"]]
   [:p "Website built for Kekoa Veteran Foundation internship"]
   [:p "Built with: HTML, CSS, Javascript, JQuery"] [:br]
   [:a {:href "https://github.com/julioberina/dab_shooter"} [:h2 "Dab Shooter"]]
   [:p "A game I built at a hackathon in which you shoot enemies with dab emojis"]
   [:p "Built with: Ruby (Gosu gem)"] [:br]
   [:a {:href "https://github.com/julioberina/caffeine"} [:h2 "Caffeine"]]
   [:p "A wonderful game about trying to stay awake with coffee and donuts"]
   [:p "Built with: Ruby (Gosu gem)"] [:br]
   [:a {:href "https://github.com/julioberina/portfolio"} [:h2 "Portfolio"]]
   [:p (str "Last but not least, my own portfolio is a project of mine. "
            "It happens to be one of my favorites")]
   [:p "Built with: Clojurescript, Reagent"] [:br]
   [:p (str "All project link to their proper Github repos. The web app "
            "projects contain the link to actual site in the README.md")] [:br]])

(defn contact-content-component []
  [:div.contact-content
   [:h1 "Contact Me"]
   [:p (str "Feel free to contact me through the email provided below. "
            "I'd love to collaborate!")]
   [:a {:href "mailto:julioberina@gmail.com"} "julioberina@gmail.com"]])

(defn navbar-component []
  [:ul.navbar
   [:li [:a {:href "/"} "Home"]]
   [:li [:a {:href "/about"} "About"]]
   [:li [:a {:href "/projects"} "Projects"]]
   [:li [:a {:href "/contact"} "Contact"]]
   [:li {:style {:float "right"}}
    [:a {:class "fa fa-github fa-2x" :href "https://github.com/julioberina"}]]
   [:li {:style {:float "right"}}
    [:a {:class "fa fa-linkedin fa-2x" :href "https://linkedin.com/in/julio-berina"}]]
   [:li {:style {:float "right"}}
    [:a {:class "fa fa-instagram fa-2x" :href "https://instagram.com/julioberina"}]]])

(defn mobile-menu-component []
  [:a {:class "fa fa-bars fa-2x"
       :style {:padding "10px" :color "white"}
       :on-click #(toggle-visibility)}])

(defn mobile-nav-component []
  [:div {:id "menu" :style {:visibility @mobile-menu-visibility}}
   [:ul {:style {:list-style-type "none"}}
    [:li [:a {:href "/"} "Home"]]
    [:li [:a {:href "/about"} "About"]]
    [:li [:a {:href "/projects"} "Projects"]]
    [:li [:a {:href "/contact"} "Contact"]]
    [:hr]
    [:li
     [:a {:class "fa fa-github fa-5x" :href "https://github.com/julioberina"}
      "  Github"]]
    [:li
     [:a {:class "fa fa-linkedin fa-5x" :href "https://linkedin.com/in/julio-berina"}
      "  LinkedIn"]]
    [:li
     [:a {:class "fa fa-instagram fa-5x" :href "https://instagram.com/julioberina"}
      "  Instagram"]]]])

;; Begin Application routing

(defroute "/" []
  (reset! current-component home-content-component)
  (set! (.-style js/document.body) "background-image :url('/img/background.jpg')"))

(defroute "/about" []
  (reset! current-component about-content-component)
  (set! (.-style js/document.body) "background-image: none"))

(defroute "/projects" []
  (reset! current-component projects-content-component)
  (set! (.-style js/document.body) "background-image: none"))

(defroute "/contact" []
  (reset! current-component contact-content-component)
  (set! (.-style js/document.body) "background-image: none"))

;; End Application routing

(defn main-component []
  [:div
   (if (mobile?)
     [:div
      [mobile-menu-component]
      (if (= @mobile-menu-visibility "visible")
        [mobile-nav-component])]
     [navbar-component])
   [:div#content
    [@current-component]]])

(defn render []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (let [div (.getElementById js/document "app")]
    (reagent/render-component [main-component] div)))
