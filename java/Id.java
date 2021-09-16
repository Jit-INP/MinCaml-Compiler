public class Id {
    String id;
    Id(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return id;
    }
    static int x = -1;
    static Id gen() {
        x++;
        return new Id("?v" + x);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Id){
            return id.equals(((Id) o).getId());
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return x;
    }
}
