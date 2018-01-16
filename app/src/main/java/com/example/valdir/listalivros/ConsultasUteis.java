package com.example.valdir.listalivros;
// Métodos auxiliares relacionados à solicitação e recebimento de dados

import android.nfc.Tag;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class ConsultasUteis {

    /**
     * Exemplo de resposta JSON de uma consulta API GOOGLE BOOKS
     */
    private static final String AMOSTRA_RESPOSTA_JSON = "{\n" +
            " \"kind\": \"books#volumes\",\n" +
            " \"totalItems\": 382,\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"7dsAAAAAQBAJ\",\n" +
            "   \"etag\": \"7qBG1sH1yN0\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/7dsAAAAAQBAJ\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Expert Android\",\n" +
            "    \"authors\": [\n" +
            "     \"Satya Komatineni\",\n" +
            "     \"Dave MacLean\",\n" +
            "     \"Phani Kanakala\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Apress\",\n" +
            "    \"publishedDate\": \"2013-07-02\",\n" +
            "    \"description\": \"From the leading publisher of Android books, Apress’ Expert Android gives you advanced techniques for customizing views, controls, and layouts. You’ll learn to develop apps in record time using JSON, Advanced Form Processing, and the BaaS (Backend As A Service) platform Parse. The book also includes extensive coverage on OpenGL, Search, and Telephony. With these advanced and time saving technologies you’ll be able to release compelling mobile applications in Google Play and the Amazon Appstore at a rapid pace. In Expert Android, you’ll learn to: Borrow, reuse, or build custom Android UI components Create 3D experiences using OpenGL ES 2.0 Write collaborative applications in the Parse cloud and communicate with your app user community through Parse Push Technology Reduce the time-to-market while creating rock solid apps for multiple devices Whether you are an individual or enterprise developer, in Expert Android you’ll find the advanced techniques and practices to take your mobile apps to the next level. Regardless of the Android release, this book serves as your definitive, capstone reference for your Apress Android experience. What you’ll learn How to deliver impactful Apps cheaper, better, and faster. How to develop for multiple devices and manage fragmentation in Android How to use Parse cloud for Storage, Collaborative Social Apps, and Push notifications How to create Custom Views, Controls, and Layouts How to create 3D experience with OpenGL ES 2.0 How to achieve speed to market through JSON, Form processing, and Parse How to eliminate memory leaks and poor-performing code Who this book is for This book is for advanced Android app developers who have read/used Pro Android already by the very same authors that bring you Expert Android. Table of Contents01. Custom Views 02. Compound Views 03. Custom Layouts 04. Using JSON for On-Device Persistence 05. Programming for multiple devices 06. Advanced Form Processing for Android 07. Using the Telephony API 08. Advanced Debugging and Analysis 09. Programming in OpenGL ES 2.0 for Android 10. Android Search User Experience 11. Android Search Providers 12. Android Search Custom Providers 13. Cloud storage for Applications: Parse.com 14. Enhancing Parse with Parcelables 15. Using Push Notifications with Parse\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9781430249504\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"1430249501\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 436,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Computers\"\n" +
            "    ],\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": false,\n" +
            "    \"contentVersion\": \"2.2.2.0.preview.3\",\n" +
            "    \"panelizationSummary\": {\n" +
            "     \"containsEpubBubbles\": false,\n" +
            "     \"containsImageBubbles\": false\n" +
            "    },\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=7dsAAAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=7dsAAAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com.br/books?id=7dsAAAAAQBAJ&pg=PA257&dq=search+android&hl=&cd=1&source=gbs_api\",\n" +
            "    \"infoLink\": \"http://books.google.com.br/books?id=7dsAAAAAQBAJ&dq=search+android&hl=&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"https://books.google.com/books/about/Expert_Android.html?hl=&id=7dsAAAAAQBAJ\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"saleability\": \"NOT_FOR_SALE\",\n" +
            "    \"isEbook\": false\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com.br/books/download/Expert_Android-sample-epub.acsm?id=7dsAAAAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://play.google.com/books/reader?id=7dsAAAAAQBAJ&hl=&printsec=frontcover&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"Search capabilities in Android extend the familiar web—based Google search \\u003cbr\\u003e\\nbar to search both device-based local content and Internet-based external \\u003cbr\\u003e\\ncontent. You can further use this search mechanism to discover and invoke \\u003cbr\\u003e\\napplications directly from the search results on the home page. Android makes \\u003cbr\\u003e\\nthese features possible by providing a search framework that allows all \\u003cbr\\u003e\\napplications to participate in the \\u003cb\\u003esearch\\u003c/b\\u003e. \\u003cb\\u003eAndroid\\u003c/b\\u003e search involves a single search \\u003cbr\\u003e\\nbox to let users enter&nbsp;...\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"XLAJXwcCRz0C\",\n" +
            "   \"etag\": \"vMYM18IOHQA\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/XLAJXwcCRz0C\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Pro Android 2\",\n" +
            "    \"authors\": [\n" +
            "     \"Sayed Hashimi\",\n" +
            "     \"Satya Komatineni\",\n" +
            "     \"Dave MacLean\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Apress\",\n" +
            "    \"publishedDate\": \"2010-08-05\",\n" +
            "    \"description\": \"Pro Android 2 shows how to build real-world and fun mobile applications using Google's latest Android software development kit. This new edition is updated for Android 2, covering everything from the fundamentals of building applications for embedded devices to advanced concepts such as custom 3D components, OpenGL, and touchscreens including gestures. While other Android development guides simply discuss topics, Pro Android 2 offers the combination of expert insight and real sample applications that work. Discover the design and architecture of the Android SDK through practical examples, and how to build mobile applications using the Android SDK. Explore and use the Android APIs, including those for media and Wi-Fi. Learn about Android 2's integrated local and web search, handwriting gesture UI, Google Translate, and text-to-speech features. Pro Android 2 dives deep, providing you with all the knowledge and techniques you need to build mobile applications ranging from games to Google apps, including add-ons to Google Docs. You'll be able to extend and run the new Google Chrome APIs on the G1, the G2, and other next-generation Google phones and Android-enabled devices.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9781430226604\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"1430226609\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 736,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Computers\"\n" +
            "    ],\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": false,\n" +
            "    \"contentVersion\": \"1.6.4.0.preview.3\",\n" +
            "    \"panelizationSummary\": {\n" +
            "     \"containsEpubBubbles\": false,\n" +
            "     \"containsImageBubbles\": false\n" +
            "    },\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=XLAJXwcCRz0C&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=XLAJXwcCRz0C&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com.br/books?id=XLAJXwcCRz0C&pg=PA492&dq=search+android&hl=&cd=2&source=gbs_api\",\n" +
            "    \"infoLink\": \"https://play.google.com/store/books/details?id=XLAJXwcCRz0C&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"https://market.android.com/details?id=book-XLAJXwcCRz0C\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"saleability\": \"FOR_SALE\",\n" +
            "    \"isEbook\": true,\n" +
            "    \"listPrice\": {\n" +
            "     \"amount\": 141.88,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"retailPrice\": {\n" +
            "     \"amount\": 141.88,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"buyLink\": \"https://play.google.com/store/books/details?id=XLAJXwcCRz0C&rdid=book-XLAJXwcCRz0C&rdot=1&source=gbs_api\",\n" +
            "    \"offers\": [\n" +
            "     {\n" +
            "      \"finskyOfferType\": 1,\n" +
            "      \"listPrice\": {\n" +
            "       \"amountInMicros\": 1.4188E8,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"retailPrice\": {\n" +
            "       \"amountInMicros\": 1.4188E8,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"giftable\": true\n" +
            "     }\n" +
            "    ]\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com.br/books/download/Pro_Android_2-sample-epub.acsm?id=XLAJXwcCRz0C&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com.br/books/download/Pro_Android_2-sample-pdf.acsm?id=XLAJXwcCRz0C&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://play.google.com/books/reader?id=XLAJXwcCRz0C&hl=&printsec=frontcover&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"\\u003cb\\u003eSearch\\u003c/b\\u003e capabilities in \\u003cb\\u003eAndroid\\u003c/b\\u003e extend the familiar web-based Google \\u003cb\\u003esearch\\u003c/b\\u003e bar \\u003cbr\\u003e\\nto \\u003cb\\u003esearch\\u003c/b\\u003e both device-based local content and Internet-based external content. \\u003cbr\\u003e\\nYou can also use this \\u003cb\\u003esearch\\u003c/b\\u003e mechanism to invoke applications directly from the \\u003cbr\\u003e\\n\\u003cb\\u003esearch\\u003c/b\\u003e bar on the home page. \\u003cb\\u003eAndroid\\u003c/b\\u003e accomplishes this by providing a \\u003cb\\u003esearch\\u003c/b\\u003e \\u003cbr\\u003e\\nframework that allows local applications to participate. \\u003cb\\u003eAndroid search\\u003c/b\\u003e protocol is \\u003cbr\\u003e\\nsimple. \\u003cb\\u003eAndroid\\u003c/b\\u003e uses a single \\u003cb\\u003esearch\\u003c/b\\u003e box to let users enter \\u003cb\\u003esearch\\u003c/b\\u003e data. This is \\u003cbr\\u003e\\ntrue&nbsp;...\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"Z0knCgAAQBAJ\",\n" +
            "   \"etag\": \"tO/g9OVoIpM\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/Z0knCgAAQBAJ\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Pro Android 5\",\n" +
            "    \"authors\": [\n" +
            "     \"Dave MacLean\",\n" +
            "     \"Satya Komatineni\",\n" +
            "     \"Grant Allen\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Apress\",\n" +
            "    \"publishedDate\": \"2015-06-15\",\n" +
            "    \"description\": \"Pro Android 5 shows you how to build real-world and fun mobile apps using the Android 5 SDK. This book updates the best-selling Pro Android and covers everything from the fundamentals of building apps for smartphones, tablets, and embedded devices to advanced concepts such as custom components, multi-tasking, sensors/augmented reality, better accessories support and much more. Using the tutorials and expert advice, you'll quickly be able to build cool mobile apps and run them on dozens of Android-based smartphones. You'll explore and use the Android APIs, including those for media and sensors. And you'll check out what's new in Android, including the improved user interface across all Android platforms, integration with services, and more. By reading this definitive tutorial and reference, you'll gain the knowledge and experience to create stunning, cutting-edge Android apps that can make you money, while keeping you agile enough to respond to changes in the future.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9781430246817\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"1430246812\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 752,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Computers\"\n" +
            "    ],\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": true,\n" +
            "    \"contentVersion\": \"1.5.4.0.preview.3\",\n" +
            "    \"panelizationSummary\": {\n" +
            "     \"containsEpubBubbles\": false,\n" +
            "     \"containsImageBubbles\": false\n" +
            "    },\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=Z0knCgAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=Z0knCgAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com.br/books?id=Z0knCgAAQBAJ&pg=PA157&dq=search+android&hl=&cd=3&source=gbs_api\",\n" +
            "    \"infoLink\": \"https://play.google.com/store/books/details?id=Z0knCgAAQBAJ&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"https://market.android.com/details?id=book-Z0knCgAAQBAJ\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"saleability\": \"FOR_SALE\",\n" +
            "    \"isEbook\": true,\n" +
            "    \"listPrice\": {\n" +
            "     \"amount\": 126.54,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"retailPrice\": {\n" +
            "     \"amount\": 126.54,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"buyLink\": \"https://play.google.com/store/books/details?id=Z0knCgAAQBAJ&rdid=book-Z0knCgAAQBAJ&rdot=1&source=gbs_api\",\n" +
            "    \"offers\": [\n" +
            "     {\n" +
            "      \"finskyOfferType\": 1,\n" +
            "      \"listPrice\": {\n" +
            "       \"amountInMicros\": 1.2654E8,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"retailPrice\": {\n" +
            "       \"amountInMicros\": 1.2654E8,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"giftable\": true\n" +
            "     }\n" +
            "    ]\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com.br/books/download/Pro_Android_5-sample-epub.acsm?id=Z0knCgAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com.br/books/download/Pro_Android_5-sample-pdf.acsm?id=Z0knCgAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://play.google.com/books/reader?id=Z0knCgAAQBAJ&hl=&printsec=frontcover&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"This section shows how to use a \\u003cb\\u003esearch\\u003c/b\\u003e widget in the action bar. You need the \\u003cbr\\u003e\\nfollowing to use \\u003cb\\u003esearch\\u003c/b\\u003e in your action bar: 1. Define a menu item in a menu XML \\u003cbr\\u003e\\nfile pointing to a \\u003cb\\u003esearch\\u003c/b\\u003e view class provided by the SDK. You also need an \\u003cbr\\u003e\\nactivity into which you can load this menu. This is often called the \\u003cb\\u003esearch\\u003c/b\\u003e invoker \\u003cbr\\u003e\\nactivity. 2. Create another activity that can take the query from the \\u003cb\\u003esearch\\u003c/b\\u003e view in \\u003cbr\\u003e\\nstep 1 and provide results. This is often called the \\u003cb\\u003esearch\\u003c/b\\u003e results activity. 3. \\u003cbr\\u003e\\nCreate an XML&nbsp;...\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"--VQBQAAQBAJ\",\n" +
            "   \"etag\": \"aemNeUIPouQ\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/--VQBQAAQBAJ\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Advanced Android Application Development\",\n" +
            "    \"authors\": [\n" +
            "     \"Joseph Annuzzi Jr.\",\n" +
            "     \"Lauren Darcey\",\n" +
            "     \"Shane Conder\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Addison-Wesley Professional\",\n" +
            "    \"publishedDate\": \"2014-11-11\",\n" +
            "    \"description\": \"Advanced Android™ Application Development, Fourth Edition, is the definitive guide to building robust, commercial-grade Android apps. Systematically revised and updated, this guide brings together powerful, advanced techniques for the entire app development cycle, including design, coding, testing, debugging, and distribution. With the addition of quizzes and exercises in every chapter, it is ideal for both professional and classroom use. An outstanding practical reference for the newest Android APIs, this guide provides in-depth explanations of code utilizing key API features and includes downloadable sample apps for nearly every chapter. Together, they provide a solid foundation for any modern app project. Throughout, the authors draw on decades of in-the-trenches experience as professional mobile developers to provide tips and best practices for highly efficient development. They show you how to break through traditional app boundaries with optional features, including the Android NDK, Google Analytics and Android Wear APIs, and Google Play Game Services. New coverage in this edition includes Integrating Google Cloud Messaging into your apps Utilizing the new Google location and Google Maps Android APIs Leveraging in-app billing from Google Play, as well as third-party providers Getting started with the Android Studio IDE Localizing language and using Google Play App Translation services Extending your app’s reach with Lockscreen widgets and DayDreams Leveraging improvements to Notification, Web, SMS, and other APIs Annuzzi has released new source code samples for use with Android Studio. The code updates are posted to the associated blog site: http://advancedandroidbook.blogspot.com/ This title is an indispensable resource for intermediate- to advanced-level Java programmers who are now developing for Android, and for seasoned mobile developers who want to make the most of the new Android platform and hardware. This revamped, newly titled edition is a complete update of Android™ Wireless Application Development, Volume II: Advanced Topics, Third Edition.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9780133892451\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"013389245X\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 624,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Computers\"\n" +
            "    ],\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": true,\n" +
            "    \"contentVersion\": \"2.3.2.0.preview.3\",\n" +
            "    \"panelizationSummary\": {\n" +
            "     \"containsEpubBubbles\": false,\n" +
            "     \"containsImageBubbles\": false\n" +
            "    },\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=--VQBQAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=--VQBQAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com.br/books?id=--VQBQAAQBAJ&pg=PA424&dq=search+android&hl=&cd=4&source=gbs_api\",\n" +
            "    \"infoLink\": \"https://play.google.com/store/books/details?id=--VQBQAAQBAJ&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"https://market.android.com/details?id=book---VQBQAAQBAJ\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"saleability\": \"FOR_SALE\",\n" +
            "    \"isEbook\": true,\n" +
            "    \"listPrice\": {\n" +
            "     \"amount\": 107.47,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"retailPrice\": {\n" +
            "     \"amount\": 107.47,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"buyLink\": \"https://play.google.com/store/books/details?id=--VQBQAAQBAJ&rdid=book---VQBQAAQBAJ&rdot=1&source=gbs_api\",\n" +
            "    \"offers\": [\n" +
            "     {\n" +
            "      \"finskyOfferType\": 1,\n" +
            "      \"listPrice\": {\n" +
            "       \"amountInMicros\": 1.0747E8,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"retailPrice\": {\n" +
            "       \"amountInMicros\": 1.0747E8,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"giftable\": true\n" +
            "     }\n" +
            "    ]\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED_FOR_ACCESSIBILITY\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://play.google.com/books/reader?id=--VQBQAAQBAJ&hl=&printsec=frontcover&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"Joseph Annuzzi Jr., Lauren Darcey, Shane Conder. Here is the \\u003cb\\u003eAndroid\\u003c/b\\u003e manifest \\u003cbr\\u003e\\nfile excerpt for the searchable Activity registration: &lt;activity \\u003cb\\u003eandroid\\u003c/b\\u003e:name=&quot;\\u003cbr\\u003e\\nSimpleSearchableActivity&quot; \\u003cb\\u003eandroid\\u003c/b\\u003e:launchMode=&quot;singleTop&quot;&gt; &lt;intent-filter&gt; &lt;\\u003cbr\\u003e\\naction \\u003cb\\u003eandroid\\u003c/b\\u003e:name=&quot;\\u003cb\\u003eandroid\\u003c/b\\u003e.intent.action.\\u003cb\\u003eSEARCH\\u003c/b\\u003e&quot; /&gt; &lt;/intent-filter&gt; &lt;intent-\\u003cbr\\u003e\\nfilter&gt; &lt;action \\u003cb\\u003eandroid\\u003c/b\\u003e:name=&quot;\\u003cb\\u003eandroid\\u003c/b\\u003e.intent.action.VIEW&quot; /&gt; &lt;/intent-filter&gt; &lt;meta\\u003cbr\\u003e\\n-data \\u003cb\\u003eandroid\\u003c/b\\u003e:name=&quot;\\u003cb\\u003eandroid\\u003c/b\\u003e.app.searchable&quot; \\u003cb\\u003eandroid\\u003c/b\\u003e:resource=&quot;&nbsp;...\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"g3hAdK1IBkYC\",\n" +
            "   \"etag\": \"noiZavgt73w\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/g3hAdK1IBkYC\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Professional Android 4 Application Development\",\n" +
            "    \"authors\": [\n" +
            "     \"Reto Meier\"\n" +
            "    ],\n" +
            "    \"publisher\": \"John Wiley & Sons\",\n" +
            "    \"publishedDate\": \"2012-04-05\",\n" +
            "    \"description\": \"Developers, build mobile Android apps using Android 4 The fast-growing popularity of Android smartphones and tablets creates a huge opportunities for developers. If you're an experienced developer, you can start creating robust mobile Android apps right away with this professional guide to Android 4 application development. Written by one of Google's lead Android developer advocates, this practical book walks you through a series of hands-on projects that illustrate the features of the Android SDK. That includes all the new APIs introduced in Android 3 and 4, including building for tablets, using the Action Bar, Wi-Fi Direct, NFC Beam, and more. Shows experienced developers how to create mobile applications for Android smartphones and tablets Revised and expanded to cover all the Android SDK releases including Android 4.0 (Ice Cream Sandwich), including all updated APIs, and the latest changes to the Android platform. Explains new and enhanced features such as drag and drop, fragments, the action bar, enhanced multitouch support, new environmental sensor support, major improvements to the animation framework, and a range of new communications techniques including NFC and Wi-Fi direct. Provides practical guidance on publishing and marketing your applications, best practices for user experience, and more This book helps you learn to master the design, lifecycle, and UI of an Android app through practical exercises, which you can then use as a basis for developing your own Android apps.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9781118237229\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"1118237226\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 864,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Computers\"\n" +
            "    ],\n" +
            "    \"averageRating\": 4.0,\n" +
            "    \"ratingsCount\": 12,\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": true,\n" +
            "    \"contentVersion\": \"2.7.7.0.preview.3\",\n" +
            "    \"panelizationSummary\": {\n" +
            "     \"containsEpubBubbles\": false,\n" +
            "     \"containsImageBubbles\": false\n" +
            "    },\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=g3hAdK1IBkYC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=g3hAdK1IBkYC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com.br/books?id=g3hAdK1IBkYC&pg=PA292&dq=search+android&hl=&cd=5&source=gbs_api\",\n" +
            "    \"infoLink\": \"https://play.google.com/store/books/details?id=g3hAdK1IBkYC&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"https://market.android.com/details?id=book-g3hAdK1IBkYC\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"saleability\": \"FOR_SALE\",\n" +
            "    \"isEbook\": true,\n" +
            "    \"listPrice\": {\n" +
            "     \"amount\": 59.3,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"retailPrice\": {\n" +
            "     \"amount\": 59.3,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"buyLink\": \"https://play.google.com/store/books/details?id=g3hAdK1IBkYC&rdid=book-g3hAdK1IBkYC&rdot=1&source=gbs_api\",\n" +
            "    \"offers\": [\n" +
            "     {\n" +
            "      \"finskyOfferType\": 1,\n" +
            "      \"listPrice\": {\n" +
            "       \"amountInMicros\": 5.93E7,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"retailPrice\": {\n" +
            "       \"amountInMicros\": 5.93E7,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"giftable\": true\n" +
            "     }\n" +
            "    ]\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com.br/books/download/Professional_Android_4_Application_Devel-sample-epub.acsm?id=g3hAdK1IBkYC&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com.br/books/download/Professional_Android_4_Application_Devel-sample-pdf.acsm?id=g3hAdK1IBkYC&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://play.google.com/books/reader?id=g3hAdK1IBkYC&hl=&printsec=frontcover&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"Having defined the Content Provider to \\u003cb\\u003esearch\\u003c/b\\u003e, you must now create an Activity \\u003cbr\\u003e\\nthat will be used to display \\u003cb\\u003esearch\\u003c/b\\u003e results. This will most commonly be a simple \\u003cbr\\u003e\\nList View-based Activity, but you can use any user interface, provided that it has a \\u003cbr\\u003e\\nmechanism for displaying \\u003cb\\u003esearch\\u003c/b\\u003e results. Users will not generally expect multiple \\u003cbr\\u003e\\n\\u003cb\\u003esearches\\u003c/b\\u003e to be added to the back stack, so it&#39;s good practice to set a \\u003cb\\u003esearch\\u003c/b\\u003e \\u003cbr\\u003e\\nActivity as “single top,” ensuring that the same instance will be used repeatedly \\u003cbr\\u003e\\nrather&nbsp;...\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"XqlMCZZjkjsC\",\n" +
            "   \"etag\": \"yeQR7SPMj8Y\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/XqlMCZZjkjsC\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Android Wireless Application Development, Portable Documents\",\n" +
            "    \"authors\": [\n" +
            "     \"Shane Conder\",\n" +
            "     \"Lauren Darcey\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Pearson Education\",\n" +
            "    \"publishedDate\": \"2010-12-15\",\n" +
            "    \"description\": \"The start-to-finish guide to Android application development: massively updated for the newest SDKs and developer techniques! This book delivers all the up-to-date information, tested code, and best practices you need to create and market successful mobile apps with the latest versions of Android. Drawing on their extensive experience with mobile and wireless development, Lauren Darcey and Shane Conder cover every step: concept, design, coding, testing, packaging, and delivery. The authors introduce the Android platform, explain the principles of effective Android application design, and present today’s best practices for crafting effective user interfaces. Next, they offer detailed coverage of each key Android API, including data storage, networking, telephony, location-based services, multimedia, 3D graphics, and hardware. Every chapter of this edition has been updated for the newest Android SDKs, tools, utilities, and hardware. All sample code has been overhauled and tested on leading devices from multiple companies, including HTC, Motorola, and ARCHOS. Many new examples have been added, including complete new applications. This new edition also adds Nine new chapters covering web APIs, the Android NDK, extending application reach, managing users, data synchronization, backups, advanced user input, and more Greatly expanded coverage of Android manifest files, content providers, app design, and testing New coverage of hot topics like Bluetooth, gestures, voice recognition, App Widgets, live folders, live wallpapers, and global search Updated 3D graphics programming coverage reflecting OpenGL ES 2.0 An all-new chapter on tackling cross-device compatibility issues, from designing for the smallest phones to the big new tablets hitting the market Even more tips and tricks to help you design, develop, and test applications for different devices A new appendix full of Eclipse tips and tricks This book is an indispensable resource for every member of the Android development team: software developers with all levels of mobile experience, team leaders and project managers, testers and QA specialists, software architects, and even marketers.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"0132481766\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9780132481762\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 750,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Computers\"\n" +
            "    ],\n" +
            "    \"averageRating\": 4.0,\n" +
            "    \"ratingsCount\": 5,\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": true,\n" +
            "    \"contentVersion\": \"2.7.5.0.preview.3\",\n" +
            "    \"panelizationSummary\": {\n" +
            "     \"containsEpubBubbles\": false,\n" +
            "     \"containsImageBubbles\": false\n" +
            "    },\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=XqlMCZZjkjsC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=XqlMCZZjkjsC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com.br/books?id=XqlMCZZjkjsC&pg=PA474&dq=search+android&hl=&cd=6&source=gbs_api\",\n" +
            "    \"infoLink\": \"https://play.google.com/store/books/details?id=XqlMCZZjkjsC&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"https://market.android.com/details?id=book-XqlMCZZjkjsC\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"saleability\": \"FOR_SALE\",\n" +
            "    \"isEbook\": true,\n" +
            "    \"listPrice\": {\n" +
            "     \"amount\": 87.79,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"retailPrice\": {\n" +
            "     \"amount\": 87.79,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"buyLink\": \"https://play.google.com/store/books/details?id=XqlMCZZjkjsC&rdid=book-XqlMCZZjkjsC&rdot=1&source=gbs_api\",\n" +
            "    \"offers\": [\n" +
            "     {\n" +
            "      \"finskyOfferType\": 1,\n" +
            "      \"listPrice\": {\n" +
            "       \"amountInMicros\": 8.779E7,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"retailPrice\": {\n" +
            "       \"amountInMicros\": 8.779E7,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"giftable\": true\n" +
            "     }\n" +
            "    ]\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED_FOR_ACCESSIBILITY\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://play.google.com/books/reader?id=XqlMCZZjkjsC&hl=&printsec=frontcover&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"This enables the user to speak the \\u003cb\\u003esearch\\u003c/b\\u003e criteria instead of type it.There are \\u003cbr\\u003e\\nseveral attributes you can add to your \\u003cb\\u003esearch\\u003c/b\\u003e configuration to enable voice \\u003cbr\\u003e\\n\\u003cb\\u003esearches\\u003c/b\\u003e.The most important attribute is voiceSearchMode, which enables voice \\u003cbr\\u003e\\n\\u003cb\\u003esearches\\u003c/b\\u003e and sets the appropriate mode:The showVoiceSearchButton value \\u003cbr\\u003e\\nenables the little voice recording button to display as part of the \\u003cb\\u003esearch\\u003c/b\\u003e dialog, \\u003cbr\\u003e\\nthe launchRecognizer value tells the \\u003cb\\u003eAndroid\\u003c/b\\u003e system to use voice recording \\u003cbr\\u003e\\nactivity, and the&nbsp;...\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"TwV45aVC14IC\",\n" +
            "   \"etag\": \"rEFfoYFgMf0\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/TwV45aVC14IC\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Android Programming Unleashed\",\n" +
            "    \"authors\": [\n" +
            "     \"B.M. Harwani\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Sams Publishing\",\n" +
            "    \"publishedDate\": \"2012-12-14\",\n" +
            "    \"description\": \"Android Programming Unleashed is the most comprehensive and technically sophisticated guide to best-practice Android development with today's powerful new versions of Android: 4.1 (Jelly Bean) and 4.0.3 (Ice Cream Sandwich). Offering the exceptional breadth and depth developers have come to expect from the Unleashed series, it covers everything programmers need to know to develop robust, high-performance Android apps that deliver a superior user experience. Leading developer trainer Bintu Harwani begins with basic UI controls, then progresses to more advanced topics, finally covering how to develop feature rich Android applications that can access Internet-based services and store data. He illuminates each important SDK component through complete, self-contained code examples that show developers the most effective ways to build production-ready code. Coverage includes: understanding the modern Android platform from the developer's standpoint… using widgets, containers, resources, selection widgets, dialogs, and fragments… supporting actions and persistence… incorporating menus, ActionBars, content providers, and databases… integrating media and animations… using web, map, and other services… supporting communication via messaging, contacts, and emails… publishing Android apps, and much more.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9780133151749\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"0133151743\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 696,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Computers\"\n" +
            "    ],\n" +
            "    \"averageRating\": 4.0,\n" +
            "    \"ratingsCount\": 1,\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": true,\n" +
            "    \"contentVersion\": \"1.5.5.0.preview.3\",\n" +
            "    \"panelizationSummary\": {\n" +
            "     \"containsEpubBubbles\": false,\n" +
            "     \"containsImageBubbles\": false\n" +
            "    },\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=TwV45aVC14IC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=TwV45aVC14IC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com.br/books?id=TwV45aVC14IC&pg=PT390&dq=search+android&hl=&cd=7&source=gbs_api\",\n" +
            "    \"infoLink\": \"https://play.google.com/store/books/details?id=TwV45aVC14IC&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"https://market.android.com/details?id=book-TwV45aVC14IC\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"saleability\": \"FOR_SALE\",\n" +
            "    \"isEbook\": true,\n" +
            "    \"listPrice\": {\n" +
            "     \"amount\": 87.79,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"retailPrice\": {\n" +
            "     \"amount\": 87.79,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"buyLink\": \"https://play.google.com/store/books/details?id=TwV45aVC14IC&rdid=book-TwV45aVC14IC&rdot=1&source=gbs_api\",\n" +
            "    \"offers\": [\n" +
            "     {\n" +
            "      \"finskyOfferType\": 1,\n" +
            "      \"listPrice\": {\n" +
            "       \"amountInMicros\": 8.779E7,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"retailPrice\": {\n" +
            "       \"amountInMicros\": 8.779E7,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"giftable\": true\n" +
            "     }\n" +
            "    ]\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED_FOR_ACCESSIBILITY\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://play.google.com/books/reader?id=TwV45aVC14IC&hl=&printsec=frontcover&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"android: title=&quot;\\u003cb\\u003eSearch\\u003c/b\\u003e&quot; \\u003cb\\u003eandroid\\u003c/b\\u003e: icon=&quot;G drawable/\\u003cb\\u003esearch\\u003c/b\\u003e&quot; \\u003cb\\u003eandroid\\u003c/b\\u003e: \\u003cbr\\u003e\\nshowAsAction=&quot;if Rooms with Text&quot; &gt; &lt;men ll&lt;group android: checkable \\u003cbr\\u003e\\nBehavior=&quot;single&quot;&gt; &lt;item android: id=&quot;G+id/search code&quot; android: title=&quot;Search \\u003cbr\\u003e\\non Code&quot; android: checked=&quot;true&quot; /&gt; &lt;item android: id=&quot;G+id/search name&quot; \\u003cbr\\u003e\\nandroid: title=&quot;Search on Name&quot; android: alphabeticshortcut-&quot;n&quot; android: \\u003cbr\\u003e\\nnumericShortcut-&quot; 6&quot; /&gt; &lt;item android: id=&quot;G+id/search price&quot; android: title=&quot;\\u003cbr\\u003e\\nSearch on Price&quot; /&gt; &lt;/group&gt; &lt;/menu&gt;&nbsp;...\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"QP7VvnhDOOsC\",\n" +
            "   \"etag\": \"Z1slGTAmzvs\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/QP7VvnhDOOsC\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Programming Android\",\n" +
            "    \"authors\": [\n" +
            "     \"Zigurd Mednieks\",\n" +
            "     \"Laird Dornin\",\n" +
            "     \"G. Blake Meike\",\n" +
            "     \"Masumi Nakamura\"\n" +
            "    ],\n" +
            "    \"publisher\": \"\\\"O'Reilly Media, Inc.\\\"\",\n" +
            "    \"publishedDate\": \"2012\",\n" +
            "    \"description\": \"Presents instructions for creating Android applications for mobile devices using Java.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9781449316648\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"1449316646\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": false,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 542,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Computers\"\n" +
            "    ],\n" +
            "    \"averageRating\": 3.5,\n" +
            "    \"ratingsCount\": 2,\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": false,\n" +
            "    \"contentVersion\": \"preview-1.0.0\",\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=QP7VvnhDOOsC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=QP7VvnhDOOsC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com.br/books?id=QP7VvnhDOOsC&pg=PA403&dq=search+android&hl=&cd=8&source=gbs_api\",\n" +
            "    \"infoLink\": \"http://books.google.com.br/books?id=QP7VvnhDOOsC&dq=search+android&hl=&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"https://books.google.com/books/about/Programming_Android.html?hl=&id=QP7VvnhDOOsC\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"saleability\": \"NOT_FOR_SALE\",\n" +
            "    \"isEbook\": false\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": false\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://play.google.com/books/reader?id=QP7VvnhDOOsC&hl=&printsec=frontcover&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"The \\u003cb\\u003eSearch\\u003c/b\\u003e Dialog floats at the top of the screen. It does not cause any change in \\u003cbr\\u003e\\nthe activity stack, so when it appears, no life cycle methods—onPause(), and so \\u003cbr\\u003e\\nforth—are called. The activity just loses input focus to the \\u003cb\\u003eSearch\\u003c/b\\u003e Dialog. If the \\u003cbr\\u003e\\nuser cancels the \\u003cb\\u003esearch\\u003c/b\\u003e by pressing the Back button, the \\u003cb\\u003eSearch\\u003c/b\\u003e Dialog closes \\u003cbr\\u003e\\nand the activity regains input focus. \\u003cb\\u003eSearch\\u003c/b\\u003e Widget The \\u003cb\\u003eSearch\\u003c/b\\u003e Widget (\\u003cbr\\u003e\\nspecifically, the SearchView class) is for \\u003cb\\u003eAndroid\\u003c/b\\u003e 3.0 (Honeycomb/ API 11) and \\u003cbr\\u003e\\nlater only.\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"Dco57IRQnLEC\",\n" +
            "   \"etag\": \"KflaA0vGjZs\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/Dco57IRQnLEC\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"The Android Book\",\n" +
            "    \"authors\": [\n" +
            "     \"Imagine Publishing\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Imagine Publishing\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9781908222510\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"1908222514\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": true,\n" +
            "    \"contentVersion\": \"1.1.1.0.preview.3\",\n" +
            "    \"panelizationSummary\": {\n" +
            "     \"containsEpubBubbles\": false,\n" +
            "     \"containsImageBubbles\": false\n" +
            "    },\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=Dco57IRQnLEC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=Dco57IRQnLEC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com.br/books?id=Dco57IRQnLEC&pg=PT167&dq=search+android&hl=&cd=9&source=gbs_api\",\n" +
            "    \"infoLink\": \"https://play.google.com/store/books/details?id=Dco57IRQnLEC&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"https://market.android.com/details?id=book-Dco57IRQnLEC\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"saleability\": \"FOR_SALE\",\n" +
            "    \"isEbook\": true,\n" +
            "    \"listPrice\": {\n" +
            "     \"amount\": 23.97,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"retailPrice\": {\n" +
            "     \"amount\": 23.97,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"buyLink\": \"https://play.google.com/store/books/details?id=Dco57IRQnLEC&rdid=book-Dco57IRQnLEC&rdot=1&source=gbs_api\",\n" +
            "    \"offers\": [\n" +
            "     {\n" +
            "      \"finskyOfferType\": 1,\n" +
            "      \"listPrice\": {\n" +
            "       \"amountInMicros\": 2.397E7,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"retailPrice\": {\n" +
            "       \"amountInMicros\": 2.397E7,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"giftable\": true\n" +
            "     }\n" +
            "    ]\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com.br/books/download/The_Android_Book-sample-epub.acsm?id=Dco57IRQnLEC&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com.br/books/download/The_Android_Book-sample-pdf.acsm?id=Dco57IRQnLEC&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://play.google.com/books/reader?id=Dco57IRQnLEC&hl=&printsec=frontcover&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"Learn to effectively use Android \\u003cb\\u003esearch Android\\u003c/b\\u003e has extensive built-in search \\u003cbr\\u003e\\ncapabilities. Learn how to use and configure the platform&#39;s search features to find \\u003cbr\\u003e\\nwhat you need fast Step 1:Start \\u003cb\\u003esearching\\u003c/b\\u003e. \\u003cb\\u003eAndroid\\u003c/b\\u003e devices all have a Search \\u003cbr\\u003e\\nbutton. This button usually looks like a little magnifying glass and may be found \\u003cbr\\u003e\\nalongside the Home and Menu buttons or, sometimes, on the physical keyboard \\u003cbr\\u003e\\nof the device. Press the Search button while on the home screen to bring up \\u003cbr\\u003e\\nAndroid&nbsp;...\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"3fSoCwAAQBAJ\",\n" +
            "   \"etag\": \"edk6hRGpb1U\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/3fSoCwAAQBAJ\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Xamarin Mobile Development for Android Cookbook\",\n" +
            "    \"authors\": [\n" +
            "     \"Matthew Leibowitz\"\n" +
            "    ],\n" +
            "    \"publisher\": \"Packt Publishing Ltd\",\n" +
            "    \"publishedDate\": \"2015-11-24\",\n" +
            "    \"description\": \"Over 80 hands-on recipes to unleash full potential for Xamarin in development and monetization of feature-packed, real-world Android apps About This Book Create a number of Android applications using the Xamarin Android platform Extensively integrate your Android devices with other Android devices to enhance your app creation experience A comprehensive guide packed with real-world scenarios and pro-level practices and techniques to help you build successful Android apps Who This Book Is For If you are a Xamarin developer who wants to create complete Android applications with Xamarin, then this book is ideal for you. No prior knowledge of Android development is needed, however a basic knowledge of C# and .NET would be useful. What You Will Learn Install and use Xamarin.Android with Xamarin Studio and Visual Studio Design an app's user interface for multiple device configurations Store and protect data in databases, files, and on the cloud Utilize lists and collections to present data to the user Communicate across the network using NFC or Bluetooth Perform tasks in the background and update the user with notifications Capture and play multimedia, such as video and audio, with the camera Implement In-App Billing and Expansion Files and deploy to the store In Detail Xamarin is used by developers to write native iOS, Android, and Windows apps with native user interfaces and share code across multiple platforms not just on mobile devices, but on Windows, Mac OS X, and Linux. Developing apps with Xamarin.Android allows you to use and re-use your code and your skills on different platforms, making you more productive in any development. Although it's not a write-once-run-anywhere framework, Xamarin provides native platform integration and optimizations. There is no middleware; Xamarin.Android talks directly to the system, taking your C# and F# code directly to the low levels. This book will provide you with the necessary knowledge and skills to be part of the mobile development era using C#. Covering a wide range of recipes such as creating a simple application and using device features effectively, it will be your companion to the complete application development cycle. Starting with installing the necessary tools, you will be guided on everything you need to develop an application ready to be deployed. You will learn the best practices for interacting with the device hardware, such as GPS, NFC, and Bluetooth. Furthermore, you will be able to manage multimedia resources such as photos and videos captured with the device camera, and so much more! By the end of this book, you will be able to create Android apps as a result of learning and implementing pro-level practices, techniques, and solutions. This book will ascertain a seamless and successful app building experience. Style and approach This book employs a step-by-step approach to Android app creation, explained in a conversational and easy-to-follow style. A wide range of examples are listed to ensure a complete understanding of how to deploy competent apps on the Android market.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9781784395872\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"1784395870\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"pageCount\": 456,\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Computers\"\n" +
            "    ],\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": true,\n" +
            "    \"contentVersion\": \"preview-1.0.0\",\n" +
            "    \"panelizationSummary\": {\n" +
            "     \"containsEpubBubbles\": false,\n" +
            "     \"containsImageBubbles\": false\n" +
            "    },\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=3fSoCwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=3fSoCwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"en\",\n" +
            "    \"previewLink\": \"http://books.google.com.br/books?id=3fSoCwAAQBAJ&pg=PA129&dq=search+android&hl=&cd=10&source=gbs_api\",\n" +
            "    \"infoLink\": \"https://play.google.com/store/books/details?id=3fSoCwAAQBAJ&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"https://market.android.com/details?id=book-3fSoCwAAQBAJ\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"saleability\": \"FOR_SALE\",\n" +
            "    \"isEbook\": true,\n" +
            "    \"listPrice\": {\n" +
            "     \"amount\": 124.99,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"retailPrice\": {\n" +
            "     \"amount\": 124.99,\n" +
            "     \"currencyCode\": \"BRL\"\n" +
            "    },\n" +
            "    \"buyLink\": \"https://play.google.com/store/books/details?id=3fSoCwAAQBAJ&rdid=book-3fSoCwAAQBAJ&rdot=1&source=gbs_api\",\n" +
            "    \"offers\": [\n" +
            "     {\n" +
            "      \"finskyOfferType\": 1,\n" +
            "      \"listPrice\": {\n" +
            "       \"amountInMicros\": 1.2499E8,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"retailPrice\": {\n" +
            "       \"amountInMicros\": 1.2499E8,\n" +
            "       \"currencyCode\": \"BRL\"\n" +
            "      },\n" +
            "      \"giftable\": true\n" +
            "     }\n" +
            "    ]\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"BR\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": true\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": true\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://play.google.com/books/reader?id=3fSoCwAAQBAJ&hl=&printsec=frontcover&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"We need to let the other activities know where to send their \\u003cb\\u003esearch\\u003c/b\\u003e intents. This \\u003cbr\\u003e\\ncan be done in two ways, one of which is to add an attribute on each activity, as \\u003cbr\\u003e\\nfollows: [MetaData( &quot;\\u003cb\\u003eandroid\\u003c/b\\u003e.app.default_searchable&quot;, Value = &quot;com.\\u003cbr\\u003e\\nxamarincookbook.SearchActivity&quot;)] 2. Or, add an assembly-level attribute for all \\u003cbr\\u003e\\nactivities: [assembly: MetaData( &quot;\\u003cb\\u003eandroid\\u003c/b\\u003e.app.default_searchable&quot;, Value = &quot;com.\\u003cbr\\u003e\\nxamarincookbook.SearchActivity&quot;)] 3. Next, we add the \\u003cb\\u003esearch\\u003c/b\\u003e view to the menu \\u003cbr\\u003e\\nresource, where&nbsp;...\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}";


    //Uma instancia do objeto ConsultasUteis não será necessária, privado
    private ConsultasUteis() {
    }



    // Retorna uma lista de objetos {@link Livro} que foi construida a partir da analise JSON
    public static ArrayList<Livro> extrairLivros() {

        // Cria um ArrayList vazio para começar a adicionar
        ArrayList<Livro> livros = new ArrayList<>();

        try {

            //crie uma lista de objetos livros com os dados correspondentes.
            JSONObject baseJsonResponse = new JSONObject(AMOSTRA_RESPOSTA_JSON);
            JSONArray livroArray = baseJsonResponse.getJSONArray("items");


            for (int i = 0; i < livroArray.length(); i++) {
                JSONObject livroAtual = livroArray.getJSONObject(i);
                JSONObject informacoes = livroAtual.getJSONObject("volumeInfo");

                //TODO implementar array para autor quando tiver mais de um

                String titulo = informacoes.getString("title");
                String autor = informacoes.getString("authors");
                String versao = informacoes.getString("contentVersion");
                String paginas = informacoes.getString("pageCount");

                /**retornar:
                 titulo -  Android para Desenvolvedores
                 autor -   LUCIO CAMILO OLIVA PEREIRA
                 versao  - 1.1.0.0.preview.1
                 paginas - 240
                 */

                Livro livro = new Livro(titulo, autor, versao, paginas);
                livros.add(livro);

            }

        }catch (JSONException e) {

            e.printStackTrace();

            // Se um erro for lançado ao executar qualquer uma das instruções acima no bloco "try"
            // com a mensagem da exceção.
            Log.e("ConsultasUteis", "Problema na analise da resposta JSON", e);
        }

        // Retorna a lista de Livros
        return livros;
    }

    //Realiza a busca de livros de acordo com a url
    public static ArrayList<Livro> buscarLivros(String requisicaoUrl) {

        // Cria um objeto URL
        URL url = criarUrl(requisicaoUrl);

        String respostaJson = null;

        // Execute a solicitação HTTP para o URL e receba uma resposta JSON de volta
        try {
            respostaJson = requisicaoHttp(url);
        } catch (IOException e) {
            Log.e("eoexception", "Erro ao fechar o fluxo de entrada", e);
        }

        // Extraia campos relevantes da resposta JSON e retorne arraylist {@link Livro}
        ArrayList<Livro> livros = extrairArrayJson(respostaJson);

        // Retornar uma lista de objetos {@link Livro}
        return livros;
    }

    /**
     * Retorna a nova URL fornecido.
     */
    private static URL criarUrl(String stringUrl) {

        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("logtag", "Erro com a criação de URL ", e);
        }
        return url;
    }

    /**
     * Faça uma solicitação HTTP para o URL fornecido e devolva um String como a resposta da consulta de livros.
     */
    private static String requisicaoHttp(URL url) throws IOException {
        String respostaJson = "";

        // Se o URL for nulo, volte mais cedo.
        if (url == null) {
            return respostaJson;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
           // urlConnection.setReadTimeout(10000 /* milisegundos */);
            //urlConnection.setConnectTimeout(15000 /* milisegundos */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Se a solicitação foi bem sucedida (código de resposta 200),
            // então leia o fluxo de entrada e analise a resposta.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                respostaJson = lerStream(inputStream);
            } else {
                Log.e("connection", "Código de resposta de erro: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("httpurlconnection", "Problema ao recuperar os resultados JSON dos livros.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return respostaJson;
    }

    /**
     * Converta o {@link InputStream} em uma String que contém toda a resposta JSON do servidor.
     */
    private static String lerStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Retornar uma lista de Objetos {@link ArrayList<Livro>}, analisando as informações da resposta livroJSON convertida em String.
     */
    private static ArrayList<Livro> extrairArrayJson(String livroJSON) {


        // Se a String JSON estiver vazio ou nulo, volte mais cedo.
        if (TextUtils.isEmpty(livroJSON)) {
            return null;
        }

        try {

            //Crie um ArrayList vazio para que possamos começar a adicionar os terremotos
            ArrayList<Livro> livros = new ArrayList<>();

            //crie uma lista de objetos livros com os dados correspondentes.
            JSONObject baseJsonResponse = new JSONObject(livroJSON);
            JSONArray livroArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < livroArray.length(); i++) {
                JSONObject livroAtual = livroArray.getJSONObject(i);
                JSONObject informacoes = livroAtual.getJSONObject("volumeInfo");

                //TODO implementar array para autor quando tiver mais de um

                String titulo = informacoes.getString("title");
                String autor = informacoes.getString("authors");
                String versao = informacoes.getString("contentVersion");
                String paginas = informacoes.getString("pageCount");

                /**retornar:
                 titulo -  Android para Desenvolvedores
                 autor -   LUCIO CAMILO OLIVA PEREIRA
                 versao  - 1.1.0.0.preview.1
                 paginas - 240
                 */

                Livro livro = new Livro(titulo, autor, versao, paginas);
                livros.add(livro);

            }

            for (int i = 0; i < livroArray.length(); i++) {
                JSONObject livroAtual = livroArray.getJSONObject(i);
                JSONObject informacoes = livroAtual.getJSONObject("volumeInfo");

                //TODO implementar array para autor quando tiver mais de um

                String titulo = informacoes.getString("title");
                String autor = informacoes.getString("authors");
                String versao = informacoes.getString("contentVersion");
                String paginas = informacoes.getString("pageCount");

                Livro livro = new Livro(titulo, autor, versao, paginas);
                livros.add(livro);
            }

            // Crie uma lista de Objetos {@link ArrayList<Livro>}
            return new ArrayList<Livro>(livros);

        } catch (JSONException e) {
            Log.e("jsonexpection", "Problema ao analisar os resultados de livros da resposta JSON", e);
        }
        return null;
    }
}


