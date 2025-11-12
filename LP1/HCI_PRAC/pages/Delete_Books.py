import streamlit as st
import pandas as pd

if "books" not in st.session_state:
    st.session_state.books = pd.DataFrame(columns = ["Title", "Author", "Year"])


st.markdown(
    """
    <h2 style = 'text-align : center;'>
    Delete Book
    </h2>
    """,
    unsafe_allow_html = True
)


if not st.session_state.books.empty:
    delete_book = st.selectbox(
        "Choose the book to delete",
        st.session_state.books["Title"].tolist()
    )

    if st.button("Delete Book", use_container_width = True):
        st.session_state.books = st.session_state.books[
            st.session_state.books['Title'] != delete_book
        ]

        st.success(f"Book {delete_book} has been deleted")
        st.balloons();

else:
    st.info("No books to delete")


