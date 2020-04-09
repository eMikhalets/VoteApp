package com.supercasual.fourtop.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_home;

    private FragmentHomeBinding binding;

    private String header = "Новости 4TOP";
    private String content = "Идет активная разработка сайта 4top. Представленный вниманию " +
            "Толковый словарь «умных слов» включает в себя специфические слова, неупотребляемые " +
            "широко. Собраны термины из культурологии, психологии, медицины, мифологии, " +
            "политологии и других научных и совсем ненаучных сфер. Также здесь попадаются " +
            "современные неологизмы и свежезаимствованные из других языков слова, еще не имеющие " +
            "твердо устоявшегося определения. Все описания максимально кратки и " +
            "конкретизированы. Если какие-то из них идут в разрез с устоявшимися " +
            "энциклопедическими трактовками, то это доказательство более широкого смыслового " +
            "значения слова. Глоссарий постоянно переосмысливается и пополняется новыми " +
            "определениями. Предупреждение: Чрезмерное использование особо умного лексикона в " +
            "обыденной речи грозит непониманием со стороны собеседников. Из интервью 2009 года:" +
            " Глоссарий – развивающийся раздел. «Научный вклад». :) Свое начало он берет давно," +
            " еще с записных книжек, куда выписывались непонятные, но красивые или звучные " +
            "слова, в основном специальные термины, значение которых, порой просто невозможно " +
            "было найти в словарях, а толкование приходилось искать в контексте его " +
            "употребления. Интересно, что и сейчас некоторые из слов Глоссария не встречаются" +
            " ни в бумажных словарях, ни в Википедии! полностью >>> Как из копеек составляются " +
            "рубли, так и из крупинок прочитанного составляется знание. Владимир Иванович Даль";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);

        binding.textHomeHeader.setText(header);
        binding.textHomeContent.setText(content);

        return binding.getRoot();
    }
}
