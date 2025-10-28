
import streamlit as st
import pandas as pd

st.set_page_config(page_title="Books Library Manager", layout="centered")
st.title("📚 Books Library Manager")

# Initialize data (System State)
if "books" not in st.session_state:
    st.session_state.books = pd.DataFrame(columns=["Title", "Author", "Genre", "Year"])

with st.expander("➕ Add New Book", expanded=True):
    col1, col2 = st.columns(2)
    with col1:
        title = st.text_input("Title")
        author = st.text_input("Author")
    with col2:
        genre = st.selectbox(
            "Genre",
            ["Fiction", "Non-Fiction", "Science", "History", "Biography", "Other"]
        )
        year = st.number_input("Year", min_value=1800, max_value=2025, step=1)

    add_btn = st.button("✅ Add Book")

    if add_btn:
        if title and author:
            new_book = {"Title": title, "Author": author, "Genre": genre, "Year": year}
            st.session_state.books = pd.concat(
                [st.session_state.books, pd.DataFrame([new_book])],
                ignore_index=True
            )
            st.success(f"Book '{title}' added!")


if not st.session_state.books.empty:
    del_title = st.selectbox(
        "🗑️ Select book to delete",
        [""] + st.session_state.books["Title"].tolist()
    )
    if del_title and st.button("Delete Selected Book"):
        st.session_state.books = st.session_state.books[
            st.session_state.books["Title"] != del_title
        ]
        st.success(f"Deleted '{del_title}' successfully!")


search = st.text_input("Search by title or author").lower()
books_to_display = st.session_state.books.copy()

if search:
    books_to_display = books_to_display[
        books_to_display["Title"].str.lower().str.contains(search) |
        books_to_display["Author"].str.lower().str.contains(search)
    ]

st.markdown("### 📖 Library Collection")

if not books_to_display.empty:
    st.dataframe(books_to_display, use_container_width=True)
else:
    st.info("No books found. Try adding some!")
