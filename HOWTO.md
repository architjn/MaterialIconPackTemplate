#Setting Up the iconpack
1. Download this github project as a zip.
2. Extract it.
3. Start Android Studio and import this project as previous android studio project.
4. Rename package name like <a href="http://stackoverflow.com/a/29092698/2828124">this</a>.
5. Setup basic Strings about app and developer in <a href="https://github.com/architjn/MaterialIconPackTemplate/blob/master/app/src/main/res/values/strings.xml">strings.xml</a>
6. Set <a href="https://github.com/architjn/MaterialIconPackTemplate/blob/master/app/src/main/res/values/cards.xml">cards.xml</a> for cards on Main App screen
7. Put 4 images to be displayed on main screen in <a href="https://github.com/architjn/MaterialIconPackTemplate/tree/master/app/src/main/res/drawable-nodpi">drawable-nodpi</a> folder, named - ic_display_icon1, ic_display_icon2, ic_display_icon3, ic_display_icon4
8. Now put icon names under right category in <a href="https://github.com/architjn/MaterialIconPackTemplate/blob/master/app/src/main/res/values/icon_pack.xml">icon_pack.xml</a>
9. <a href="http://i.imgur.com/WSgSwkN.png">Fork</a> the github project and edit <a href="http://i.imgur.com/jCjzSQC.png">wallpapers.json file</a> in home directory. Now get the url of your wallpapers.json file by clicking <a href="http://i.imgur.com/YJhvVtw.png">Raw button</a> and replace new url in <a href="https://github.com/architjn/MaterialIconPackTemplate/blob/master/app/src/main/res/values/strings.xml">strings.xml</a> at line 26. Wallpaper section is ready now.
9. And at last change <a href="https://github.com/architjn/MaterialIconPackTemplate/blob/master/app/src/main/res/values/changelog.xml">changelog.xml</a>


###Easy 10 steps to initialize this icon pack


####How to add dynamic icons for calender
1. Instead of \<item\> tag in appfilter.xml use tag \<calender\> and instead of attribute **drawable** use **prefix**
2. Suppose icon name for calender is calender_icon_1.png, calender_icon_2.png, calender_icon_3.png .... so on (where numbers are dates). So now in prefix use "calender_icon_".

*That is it*<br>
**You can see commented code in appfilter.xml for example**