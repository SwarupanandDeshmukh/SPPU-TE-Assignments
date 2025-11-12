# import streamlit as st

# st.set_page_config(page_title="Books Library", layout="wide")


# st.markdown(
#     "<h1 style='text-align:center; margin-bottom:20px;'>🏠 Welcome to Your Library</h1>",
#     unsafe_allow_html=True
# )


# st.markdown(
#     """
#     <div style='text-align:justify; font-size:1.2rem; color:white; line-height:1.6; max-width:800px; margin:auto;'>
#     Today's Quotes 👇 <br><br>
#       <em>Don’t watch the clock; do what it does. Keep going. – Sam Levenson</em><br>
#     <em>Books are a uniquely portable magic. – Stephen King</em><br>
#     <em>So many books, so little time. – Frank Zappa</em>
#     </div>
#     """,
#     unsafe_allow_html=True
# )


import streamlit as st;
import pandas as pd;

st.set_page_config(page_title="Books Library", layout = "wide");

st.markdown(
    """
    <h1 style = 'text-align:center; color: white; margin:auto;'>
     🏡 Welcome to our Library.<br>
    </h1>
    """,
    unsafe_allow_html=True
)

st.markdown(
    """
    <div style = 'text-align: justify; font-size: 20px; margin:auto; '>
    Today's Quotes 👇 <br><br>
    <em> &nbsp;&nbsp;&nbsp;&nbsp;Don't watch the clock. Do what it does. - Sam Levenson </em> <br>
    <em> &nbsp;&nbsp;&nbsp;&nbsp;so many books. such little time - Frank Zappa </em> <br>
    </div>
    """,
    unsafe_allow_html = True
)