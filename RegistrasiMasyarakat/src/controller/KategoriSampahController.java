package controller;

import model.UserModel;
import view.HalamanAwalView;
import view.KategoriSampahView;
import model.KategoriMapper;
import model.KategoriModel;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import util.MyBatisUtil;
import java.util.List;

public class KategoriSampahController {
    private KategoriSampahView view;
    private SqlSessionFactory factory;

    public KategoriSampahController(SqlSessionFactory factory) {
        this.factory = factory;
        loadAndDisplayKategoriSampah();
    }

    private void loadAndDisplayKategoriSampah() {
        try (SqlSession session = factory.openSession()) {
            KategoriMapper mapper = session.getMapper(KategoriMapper.class);
            List<KategoriModel> kategoriList = mapper.getAllKategori();

            // Create view with the kategori list
            this.view = new KategoriSampahView(kategoriList);

            // Add back button listener
            this.view.addBackButtonListener(e -> {
                view.dispose();
                UserModel dummyModel = new UserModel();
                HalamanAwalView halamanAwalView = new HalamanAwalView();
                UserController userController = new UserController(dummyModel, halamanAwalView);
                userController.openHalamanUtamaView();
            });
        } catch (Exception e) {
            e.printStackTrace();
            // If there's an error loading data, show empty view
            this.view = new KategoriSampahView(null);
        }
    }

    public void showKategoriSampah() {
        view.setVisible(true);
    }
}