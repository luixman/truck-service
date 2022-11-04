package ru.truckfollower.service.polygon;

import com.sun.xml.bind.v2.TODO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import java.util.ArrayList;
import java.util.List;


@ToString
public class Polygon {
    @Getter
    List<Point> polygon = new ArrayList<>();

    //Расчет минимальных и максимальных диапазонов для оптимизации сравнения
    private Point min;
    private Point max;

    public Polygon(List<Point> polygon) {

        if (polygon.size() < 3)
            throw new IllegalArgumentException("minimum list size: 3 elements");
        this.polygon = polygon;
        rangeCalculation();

    }

    private void rangeCalculation() {
        double minX = polygon.get(0).getX();
        double minY = polygon.get(0).getY();
        double maxX = polygon.get(0).getX();
        double maxY = polygon.get(0).getY();

        for (Point p : polygon) {
            if (p.getX() < minX)
                minX = p.getX();

            if (p.getY() < minY)
                minY = p.getY();

            if (p.getX() > maxX)
                maxX = p.getX();

            if (p.getY() > maxY)
                maxY = p.getY();
        }
        min = new Point(minX, minY);
        max = new Point(maxX, maxY);
    }

    public boolean contains(Point p) {
        if (p.getX() > min.getX() && p.getY() > min.getY() && p.getX() < max.getX() && p.getY() < max.getY())
            return deepContains(p);

        return false;
    }

    private boolean deepContains(Point p) {
        boolean res = false;
        int j = polygon.size() - 1;

        for (int i = 0; i < polygon.size(); i++) {
            if ((((polygon.get(i).getY() < p.getY()) && (p.getY() < polygon.get(j).getY())) || ((polygon.get(j).getY() < p.getY()) && (p.getY() < polygon.get(i).getY()))) &&
                    (p.getX() >= (polygon.get(j).getX() - polygon.get(i).getX()) * (p.getY() - polygon.get(i).getY()) / (polygon.get(j).getY() - polygon.get(i).getY()) + polygon.get(i).getX()))
                res = !res;
            j = i;
        }

        return res;
    }

    // TODO: 04.11.2022 добавить диапазоны, некий хешкод, DONE
    //  который будет ускорять сравнивание, диапазон будет из 4 переменных, максимальный и минимальный х и у. DONE
    //  Если точка в этом диапазоне, то применять более сложный алгоритм сравнения.
    //  сделать класс Immutable

}