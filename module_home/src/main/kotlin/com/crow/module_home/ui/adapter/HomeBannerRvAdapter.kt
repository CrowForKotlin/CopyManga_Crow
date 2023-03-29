package com.crow.module_home.ui.adapter

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crow.base.app.appContext
import com.crow.base.current_project.entity.BookType
import com.crow.base.tools.extensions.clickGap
import com.crow.module_home.databinding.HomeBannerRvBinding
import com.crow.module_home.databinding.HomeBannerRvBinding.inflate
import com.crow.module_home.model.resp.homepage.Banner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeBannerRvAdapter(
    private var mBannerList: MutableList<Banner> = mutableListOf(),
    inline val onTap: (BookType, String) -> Unit,
) : RecyclerView.Adapter<HomeBannerRvAdapter.ViewHolder>() {

    inner class ViewHolder(val rvBinding: HomeBannerRvBinding) : RecyclerView.ViewHolder(rvBinding.root) {
        var mPathword: String = ""
    }

    override fun getItemCount(): Int = mBannerList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflate(from(parent.context), parent, false)).also { vh ->
            vh.rvBinding.baneerImage.clickGap { _, _ -> onTap(BookType.Banner, vh.mPathword) }
        }
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val banner = mBannerList[position]
        Glide.with(appContext)
            .load(banner.mImgUrl)
            .into(vh.rvBinding.baneerImage)
        vh.rvBinding.bannerText.text = banner.mBrief
        vh.mPathword = banner.mComic!!.mPathWord
    }

    suspend fun doBannerNotify(banners: MutableList<Banner>, delay: Long) {
        val isCountSame = itemCount == banners.size
        if (isCountSame) mBannerList = banners
        else if(itemCount != 0) {
            notifyItemRangeRemoved(0, itemCount)
            mBannerList.clear()
        }
        banners.forEachIndexed { index, data ->
            if (!isCountSame) {
                mBannerList.add(data)
                notifyItemInserted(index)
            } else notifyItemChanged(index)
            delay(delay)
        }
    }

    /*suspend fun doOnNotify(datas: List<Banner>, delay: Long = 20L, waitTime: Long = 100L) {
        mBannerList.clear()
        datas.forEachIndexed { index, banner ->
            if (banner.mType <= 2) mBannerList.add(banner)
            notifyItemChanged(index)
        }
    }*/
}