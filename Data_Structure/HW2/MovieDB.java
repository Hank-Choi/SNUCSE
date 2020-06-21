//MovieDB안에 GenreList가 있고 GenreList속 각각의 장르에는 그 장르에 속한 MovieDBItem의 목록이 있다.
public class MovieDB {
	private MyLinkedList<Genre> genreList =new MyLinkedList<Genre>();
	
	public MyLinkedList<Genre> getGenreList(){return this.genreList;}
	
    public MovieDB() {
        // FIXME implement this
    	
    	// HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한 
    	// MyLinkedList 타입의 멤버 변수를 초기화 한다.
    }

    public void insert(MovieDBItem item) {
		Genre insertGenre = new Genre(item.getGenre());
    	insertGenre.getMovieList().add(item);
    	for(MyLinkedListIterator<Genre> genreIter=new MyLinkedListIterator<Genre>(getGenreList());;) {
    		if(!genreIter.hasNext()) {
    			genreIter.add(insertGenre);
				break;
			}
    		genreIter.next();
    		final int genreDiff= genreIter.getcurr().getItem().compareTo(insertGenre);
    		//GenreIter의 curr이 가르키는 Genre와 삽입하려는 Genre의 우선 순위를 나타내는 상수.
    		if(genreDiff==0) {
    			MyLinkedList<MovieDBItem> movies=genreIter.getcurr().getItem().getMovieList();
				if(movies.isEmpty()) {
					movies.add(item);
					break;
				}
    			for(MyLinkedListIterator<MovieDBItem> movieIter=new MyLinkedListIterator<MovieDBItem>(movies);;) {
    				if(!movieIter.hasNext()) {
    					movieIter.add(item);
    					break;
    				}
    				movieIter.next();
    				final int titleDiff= movieIter.getcurr().getItem().compareTo(item);
    				if(titleDiff>0) {
    					movieIter.insert(item);
    					break;
    				}
    				else if(titleDiff==0) {
    					break;
    				}
    			}
    			break;
    		} else if(genreDiff>0) {
    			genreIter.insert(insertGenre);
    			break;
    		}
    	}
    	//GenreList를 Iterator를 통해 비교 후 적절한 위치에 삽입, 이름이 같은 Genre가 있으면 그 Genre의 MovieList를 Iterator를 통해 비교 후 적절한 위치에 삽입
    }
	public void delete(MovieDBItem item) {
		MovieDBItem deleteMovie = item;
    	Genre deleteGenre = new Genre(item.getGenre());
    	for(MyLinkedListIterator<Genre> genreIter=new MyLinkedListIterator<Genre>(getGenreList());genreIter.hasNext();) {
    		genreIter.next();
    		if(genreIter.getcurr().getItem().equals(deleteGenre)) {
    			MyLinkedList<MovieDBItem> Movies=genreIter.getcurr().getItem().getMovieList();
    			for(MyLinkedListIterator<MovieDBItem> MovieIter=new MyLinkedListIterator<MovieDBItem>(Movies);MovieIter.hasNext();) {
    				MovieIter.next();
    				if(MovieIter.getcurr().getItem().compareTo(deleteMovie)==0) {
    					MovieIter.remove();
    					if(Movies.isEmpty()) {genreIter.remove();}
    					break;
    				}
    			}
    			break;
    		}
    	}
    	//Iterator를 통해 GenreList에서 이름이 같은 Genre를 찾고 그 Genre의 MovieList에서 이름이 같은 MovieDBItem을 찾아 삭제, MovieList가 비면 장르도 삭제.
    }

    public MyLinkedList<MovieDBItem> search(String term) {
    	MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
    	MyLinkedListIterator<MovieDBItem> resultIter=new MyLinkedListIterator<MovieDBItem>(results);
    	for(Genre i:genreList) {
			for(MovieDBItem j:i.getMovieList()) {
				if(j.getTitle().contains(term))
					resultIter.add(j);
			}
    	}
       return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
    	MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
    	MyLinkedListIterator<MovieDBItem> resultIter=new MyLinkedListIterator<MovieDBItem>(results);
    	for(Genre i:getGenreList()) {
			for(MovieDBItem j:i.getMovieList()) {
				resultIter.add(j);
			}
    	}
    	return results;
    }
}

class Genre implements Comparable<Genre> {
	private MyLinkedList<MovieDBItem> MovieList=new MyLinkedList<MovieDBItem>();
	//장르 각각에 그 장르의 영화 목록을 contain
	private String name;
	public Genre(String name) {
		this.name=name;
	}
	public MyLinkedList<MovieDBItem> getMovieList(){return this.MovieList;}

	@Override
	public int compareTo(Genre o) {
		return this.name.compareTo(o.name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genre other = (Genre) obj;
		if (name == null) {
			if (other.name != null)
				return false;
	        } else if (!name.equals(other.name))
	        	return false;
		return true;
	}
}