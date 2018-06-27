package com.tomatron

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Generic adapter for binding cells to the view.
 *
 * @see https://github.com/ThomasJones/Android-RecyclerCell
 */
class RecyclerCellAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var cells = listOf<RecyclerCell>()
        set(value) {
            field = value

            viewHolderCreators.clear()
            cells.forEach {
                viewHolderCreators.put(it.viewType, it)
            }
            notifyDataSetChanged()
        }

    private val viewHolderCreators = SparseArray<RecyclerCell>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return viewHolderCreators.get(viewType).createViewHolder(parent, inflater)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        cells[position].bindTo(holder)
    }

    override fun getItemCount(): Int {
        return cells.size
    }

    override fun getItemViewType(position: Int): Int {
        return cells[position].viewType
    }

    fun createSpanSizeLookUp(defaultSpan: Int): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return cells[position].getSpanSize(defaultSpan)
            }
        }
    }
}